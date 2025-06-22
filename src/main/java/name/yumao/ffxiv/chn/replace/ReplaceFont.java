package name.yumao.ffxiv.chn.replace;

import name.yumao.ffxiv.chn.builder.BinaryBlockBuilder;
import name.yumao.ffxiv.chn.builder.TexBlockBuilder;
import name.yumao.ffxiv.chn.model.SqPackIndex;
import name.yumao.ffxiv.chn.model.SqPackIndexFile;
import name.yumao.ffxiv.chn.model.SqPackIndexFolder;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.util.FFCRC;
import name.yumao.ffxiv.chn.util.LERandomAccessFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;


public class ReplaceFont {

	private final String outputFolder;
	private final String resourceFolder;
	private final PercentPanel percentPanel;

	public ReplaceFont(String outputFolder, String resourceFolder,PercentPanel percentPanel) {
		this.outputFolder = outputFolder;
		this.resourceFolder = resourceFolder;
		this.percentPanel = percentPanel;
	}

	public void replace() throws Exception {

		System.out.println("Loading Index File...");
		HashMap<Integer, SqPackIndexFolder> index = new SqPackIndex(outputFolder).resloveIndex();
		System.out.println("Loading Index Complete");

		LERandomAccessFile leIndexFile = new LERandomAccessFile(outputFolder, "rw");
		LERandomAccessFile leDatFile = new LERandomAccessFile(outputFolder.replace("index", "dat0"), "rw");

		long datLength = leDatFile.length();
		leDatFile.seek(datLength);
		int fileCount=0;
		//走訪清單檔案
		File resourceFolderFile = new File(resourceFolder);
		if (resourceFolderFile.isDirectory()) {
			for (File resourceFile : resourceFolderFile.listFiles()) {
				percentPanel.percentShow((double)(++fileCount) / (double)resourceFolderFile.listFiles().length, "Replace : " + resourceFile.getName());
				if (resourceFile.isFile()) {
					//read file
					LERandomAccessFile lera = new LERandomAccessFile(resourceFile, "r");
					byte[] data = new byte[(int) lera.length()];
					lera.readFully(data);
					lera.close();
					//build block
					byte[] block = new byte[0];
					if(resourceFile.getName().endsWith(".tex")) {
						//type 4
						block = new TexBlockBuilder(data).buildBlock();
					}else {
						block = new BinaryBlockBuilder(data).buildBlock();
					}
					//add index
					System.out.println("Replace : " + resourceFile.getName());
					Integer folderCRC = FFCRC.ComputeCRC(("common/font").toLowerCase().getBytes());
					Integer fileCRC = FFCRC.ComputeCRC((resourceFile.getName()).toLowerCase().getBytes());
					SqPackIndexFile indexFile = index.get(folderCRC).getFiles().get(fileCRC);
					leIndexFile.seek(indexFile.getPt() + 8);
					leIndexFile.writeInt((int) (datLength / 8));
					//add dat
					datLength += block.length;
					leDatFile.write(block);
				}
			}
		}
		leDatFile.close();
		leIndexFile.close();
	}
	
	public static void main(String[] args) throws Exception {

	}
}
