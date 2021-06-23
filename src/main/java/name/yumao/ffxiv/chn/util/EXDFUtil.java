package name.yumao.ffxiv.chn.util;

import name.yumao.ffxiv.chn.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EXDFUtil {

    private final String pathToIndexSE;
    private List<String> fileList;

    public EXDFUtil(String pathToIndexSE) {
        this.pathToIndexSE = pathToIndexSE;
    }

    public EXDFUtil(String pathToIndexSE, List<String> fileList) {
        this.pathToIndexSE = pathToIndexSE;
        this.fileList = fileList;
    }


    public boolean isTransDat() throws Exception {
        if (pathToIndexSE == null) return false;
        HashMap<Integer, SqPackIndexFolder> indexSE = new SqPackIndex(pathToIndexSE).resloveIndex();
        String replaceFile = "EXD/Addon";
        // 准备好文件目录名和文件名
        String filePatch = replaceFile.substring(0, replaceFile.lastIndexOf("/"));
        String fileName = replaceFile.substring(replaceFile.lastIndexOf("/") + 1) + ".EXH";
        // 计算文件目录CRC
        Integer filePatchCRC = FFCRC.ComputeCRC(filePatch.toLowerCase().getBytes());
        // 计算头文件CRC
        Integer exhFileCRC = FFCRC.ComputeCRC(fileName.toLowerCase().getBytes());
        // 解压并且解析头文件
        if (indexSE.get(filePatchCRC) == null) return false;
        SqPackIndexFile exhIndexFileSE = indexSE.get(filePatchCRC).getFiles().get(exhFileCRC);
        if (exhIndexFileSE == null) return false;
        byte[] exhFileSE = extractFile(pathToIndexSE, exhIndexFileSE.getOffset());
        EXHFFile exhSE = new EXHFFile(exhFileSE);
        if (exhSE.getLangs().length > 0) {
            // 根据头文件 轮询资源文件
            for (EXDFPage exdfPage : exhSE.getPages()) {
                // 获取资源文件的CRC
                Integer exdFileCRCJA = FFCRC.ComputeCRC((fileName.replace(".EXH", "_" + exdfPage.pageNum + "_JA.EXD")).toLowerCase().getBytes());
                // 提取对应的文本文件
                SqPackIndexFile exdIndexFileJA = indexSE.get(filePatchCRC).getFiles().get(exdFileCRCJA);
                byte[] exdFileJA = null;
                try {
                    exdFileJA = extractFile(pathToIndexSE, exdIndexFileJA.getOffset());
                } catch (Exception jaEXDFileException) {
                    continue;
                }
                // 解压本文文件 提取内容
                EXDFFile ja_exd = new EXDFFile(exdFileJA);
                HashMap<Integer, byte[]> jaExdList = ja_exd.getEntrys();
                EXDFEntry exdfEntryJA = new EXDFEntry(jaExdList.get(5506), exhSE.getDatasetChunkSize());
                for (EXDFDataset exdfDatasetSE : exhSE.getDatasets()) {
                    // 只限文本内容
                    if (exdfDatasetSE.type == 0) {
                        byte[] jaBytes = exdfEntryJA.getString(exdfDatasetSE.offset);
                        String jaStr = new String(jaBytes, StandardCharsets.UTF_8);
                        if(jaStr.contains("teemo.link") || jaStr.contains("sdo.com")){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private byte[] extractFile(String pathToIndexSE, long dataOffset) throws IOException {
        String pathToOpen = pathToIndexSE;
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
