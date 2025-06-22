package name.yumao.ffxiv.chn.replace;

import com.opencsv.CSVWriter;
import name.yumao.ffxiv.chn.model.*;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.util.FFCRC;
import name.yumao.ffxiv.chn.util.FFXIVString;
import name.yumao.ffxiv.chn.util.LERandomBytes;
import name.yumao.ffxiv.chn.util.res.Config;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class UnpackEXDF {

	private final String pathToIndexSE;
	private final List<String> fileList;
	private final PercentPanel percentPanel;

	private final String lang;

	public UnpackEXDF(String pathToIndexSE,PercentPanel percentPanel ) {
		this.pathToIndexSE = pathToIndexSE;
		this.fileList = new ArrayList<String>();
		this.percentPanel=percentPanel;
		this.lang = Config.getProperty("Language");
	}
	public void unpack(String unpackPath) throws Exception {
		System.out.println(String.format("Unpack Start : %s",pathToIndexSE));
		File rootPath = new File("output"+File.separator+unpackPath);
		System.out.println(String.format("Unpack To : %s",rootPath.getAbsolutePath()));
		System.out.println("Loading Root File...");
		//取得檔案清單
		this.initFileList();
		//載入索引
		System.out.println("Loading Index File...");
		HashMap<Integer, SqPackIndexFolder> indexSE = new SqPackIndex(pathToIndexSE).resloveIndex();
		System.out.println("Loading Index Complete");
		//走訪清單檔案
		int fileCount=0;
		for (String unpackFile : fileList) {
			percentPanel.percentShow((double)(++fileCount) / (double)fileList.size(),unpackFile);
			//只看head描述檔
			if (unpackFile.toUpperCase().endsWith(".EXH")) {
				// 切開路徑和檔名以供計算CRC hashmap
				String filePath = unpackFile.substring(0, unpackFile.lastIndexOf("/"));
				String fileName = unpackFile.substring(unpackFile.lastIndexOf("/") + 1);
				// 計算路徑 hash
				Integer filePathCRC = FFCRC.ComputeCRC(filePath.toLowerCase().getBytes());
				// 計算描述檔 hash
				Integer exhFileCRC = FFCRC.ComputeCRC(fileName.toLowerCase().getBytes());
				//透過hash搜尋路徑
				if(indexSE.get(filePathCRC) == null) {
					System.out.println("Path Fail : " +filePath+File.separator+ filePath);
					continue;
				}
				//透過hash搜尋描述檔位址
				SqPackIndexFile exhIndexFileSE = indexSE.get(filePathCRC).getFiles().get(exhFileCRC);
				if (exhIndexFileSE == null) {
					System.out.println("Header Fail : " +filePath+File.separator+ fileName);
					continue;
				}
				// 讀取描述檔
				byte[] exhFileSE = extractFile(pathToIndexSE, exhIndexFileSE.getOffset());
				// 分析描述檔
				EXHFFile exhSE = new EXHFFile(exhFileSE);
				//只取有語言資訊的描述檔
				if (exhSE.getLangs().length > 0 && exhSE.getLangs()[0]!=0) {
					// 分頁編號
					for (EXDFPage exdfPage : exhSE.getPages()) {
						// 準備資源檔名稱
						String exdFileName = (fileName.replace(".EXH", "_" + exdfPage.pageNum + "_"+lang+".EXD")).toLowerCase();
						// 計算資源檔 hash
						Integer exdFileCRCJA = FFCRC.ComputeCRC(exdFileName.getBytes());
						// 透過hash搜尋資源檔位址
						SqPackIndexFile exdIndexFileJA = indexSE.get(filePathCRC).getFiles().get(exdFileCRCJA);
						byte[] exdFileJA = null;
						try {
							//讀取資源檔
							exdFileJA = extractFile(pathToIndexSE, exdIndexFileJA.getOffset());
						}catch (Exception jaEXDFileException){
							System.out.println("Unpack Fail : " + Arrays.toString(exhSE.getLangs()));
							System.out.println("Unpack Fail : " +filePath+File.separator+ exdFileName);
							continue;
						}
						// 分析資源檔
						EXDFFile ja_exd = new EXDFFile(exdFileJA);
						//準備輸出CSV

						File createPath = new File("output"+File.separator+unpackPath+File.separator+filePath +File.separator+ exdFileName+".csv");
						createPath.getParentFile().mkdirs();
						OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(createPath.getAbsolutePath()),"UTF-8");
						CSVWriter exportCSV = new CSVWriter(fileWriter, ',', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END );
						//載入每行資料
						HashMap<Integer, byte[]> jaExdList = ja_exd.getEntrys();
						for (Entry<Integer, byte[]> listEntry : jaExdList.entrySet()) {
							List<String> fileValues = new ArrayList<String>();
							//編號
							Integer listEntryIndex = listEntry.getKey();
							fileValues.add( listEntryIndex.toString());
							// 分析每行資料
							EXDFEntry exdfEntryJA = new EXDFEntry(listEntry.getValue(), exhSE.getDatasetChunkSize());
							for ( EXDFDataset exdfDatasetSE : exhSE.getDatasets()) {
								//分析每欄資料
								if (exdfDatasetSE.type >= 0x19)
								{
									boolean bool = exdfEntryJA.getByteBool(exdfDatasetSE.type, exdfDatasetSE.offset);
									fileValues.add( bool?"True":"False");
								}
								else
								{
									switch (exdfDatasetSE.type)
									{
										case 0x0b: // QUAD
											int[] quad = exdfEntryJA.getQuad(exdfDatasetSE.offset);
											fileValues.add(quad[3] + ", " + quad[2] + ", " + quad[1] + ", " + quad[0]);
											break;
										case 0x09: // FLOAT
											//case 0x08:
											fileValues.add(String.valueOf(exdfEntryJA.getFloat(exdfDatasetSE.offset)));
											break;
										case 0x07: // UINT
											fileValues.add(String.valueOf( (long)exdfEntryJA.getInt(exdfDatasetSE.offset) & 0xFFFFFFFF));
											break;
										case 0x06: // INT
											fileValues.add(String.valueOf( exdfEntryJA.getInt(exdfDatasetSE.offset)));
											break;
										case 0x05: // USHORT
											fileValues.add(String.valueOf( (int)exdfEntryJA.getShort(exdfDatasetSE.offset) & 0xFFFF));
											break;
										case 0x04: // SHORT
											fileValues.add(String.valueOf( exdfEntryJA.getShort(exdfDatasetSE.offset)));
											break;
										case 0x03: // UBYTE
											fileValues.add(String.valueOf( ((int)exdfEntryJA.getByte(exdfDatasetSE.offset)) & 0xFF));
											break;
										case 0x02: // BYTE
											fileValues.add(String.format("%X",exdfEntryJA.getByte(exdfDatasetSE.offset)));
											break;
										case 0x01: // BOOL
											fileValues.add( exdfEntryJA.getBoolean(exdfDatasetSE.offset)?"True":"False");
											break;
										case 0x00: // STRING; Points to offset from end of exdfDatasetSE part. Read until 0x0.
											byte[] jaBytes = exdfEntryJA.getString(exdfDatasetSE.offset);
											String jaFFStr = FFXIVString.parseFFXIVString(jaBytes);
											fileValues.add(jaFFStr);
											break;
										default:
											fileValues.add("?"+((int)exdfEntryJA.getByte(exdfDatasetSE.offset)&0xFF));
											break;
									}
								}
							}
							//寫入CSV
							String[] array = fileValues.toArray(new String[0]);
							exportCSV.writeNext(array);

						}
						fileWriter.close();

					}
				}
			}
		}
		System.out.println("Unpack Complete");
	}

	private void initFileList() throws Exception{
		HashMap<Integer, SqPackIndexFolder> indexSE = new SqPackIndex(pathToIndexSE).resloveIndex();
		Integer filePathCRC = FFCRC.ComputeCRC("exd".toLowerCase().getBytes());
		Integer rootFileCRC = FFCRC.ComputeCRC("root.exl".toLowerCase().getBytes());
		SqPackIndexFile rootIndexFileSE = indexSE.get(filePathCRC).getFiles().get(rootFileCRC);
		byte[] rootFile = extractFile(pathToIndexSE, rootIndexFileSE.getOffset());
		BufferedReader rootBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rootFile)));
		String fileName;
		while((fileName = rootBufferedReader.readLine()) != null){
			if (fileName.contains("EXLT,2")) continue;
			fileList.add("EXD/" + (fileName.contains(",") ? fileName.split(",")[0] : fileName) + ".EXH");
		}
	}

	private static void parsePayload(LERandomBytes inBytes, LERandomBytes outBytes) {
		int possition = inBytes.position();
		int type = inBytes.readByte() & 0xFF;
		int size = getBodySize((inBytes.readByte() & 0xFF), inBytes);
		byte[] body = new byte[size - 1];
		inBytes.readFully(body);
		inBytes.skip();
		long fullLength = inBytes.position() - possition + 1;
		byte[] full = new byte[(int)fullLength];
		inBytes.seek(possition - 1);
		inBytes.readFully(full);
		outBytes.write(full);
	}

	private static int getBodySize(int payloadSize, LERandomBytes inBytes) {
		if (payloadSize < 0xF0)
			return payloadSize;
		switch (payloadSize){
			case 0xF0:
				return inBytes.readInt8();
			case 0xF1:
			case 0xF2:
				return inBytes.readInt16();
			case 0xFA:
				return inBytes.readInt24();
			case 0xFE:
				return inBytes.readInt32();
		}
		return payloadSize;
	}

	private byte[] extractFile(String pathToIndex, long dataOffset) throws IOException {
		String pathToOpen = pathToIndex;
		int datNum = (int) ((dataOffset & 0xF) / 2L);
		dataOffset -= (dataOffset & 0xF);
		pathToOpen = pathToOpen.replace("index2", "dat" + datNum);
		pathToOpen = pathToOpen.replace("index", "dat" + datNum);
		SqPackDatFile datFile = new SqPackDatFile(pathToOpen);
		byte[] data = datFile.extractFile(dataOffset * 8L, false);
		datFile.close();
		return data;
	}
}
