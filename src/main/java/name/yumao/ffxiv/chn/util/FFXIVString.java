//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package name.yumao.ffxiv.chn.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Vector;

public class FFXIVString {
    final static int START_BYTE 		   = 0x02;

    final static int SIZE_DATATYPE_BYTE    = 0xF0;
    final static int SIZE_DATATYPE_BYTE256 = 0xF1;
    final static int SIZE_DATATYPE_INT16   = 0xF2;
    final static int SIZE_DATATYPE_INT24   = 0xFA;
    final static int SIZE_DATATYPE_INT32   = 0xFE;

    public static String parseFFXIVString(byte[] bytes){
        try{
            LERandomBytes inBytes = new LERandomBytes(bytes, true, false);
            LERandomBytes outBytes = new LERandomBytes();
            while (inBytes.hasRemaining()){
                byte b = inBytes.readByte();
                if (b == START_BYTE) {
                    parsePayload(inBytes, outBytes);
                } else {
                    outBytes.writeByte(b);
                }
            }
            return new String(outBytes.getWork(), StandardCharsets.UTF_8);
        }catch (Exception e){
            return "<unk:" + HexUtils.bytesToHexStringWithOutSpace(bytes) + ">";
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

        String outString = null;

        outString = "<hex:" + HexUtils.bytesToHexStringWithOutSpace(full) + ">";
        outBytes.write(outString);

    }

    private static int getBodySize(int payloadSize, LERandomBytes inBytes) {
        if (payloadSize < 0xF0)
            return payloadSize;
        switch (payloadSize){
            case SIZE_DATATYPE_BYTE:
                return inBytes.readInt8();
            case SIZE_DATATYPE_BYTE256:
            case SIZE_DATATYPE_INT16:
                return inBytes.readInt16();
            case SIZE_DATATYPE_INT24:
                return inBytes.readInt24();
            case SIZE_DATATYPE_INT32:
                return inBytes.readInt32();
        }
        return payloadSize;
    }

    public static byte[] fstr2bytes(String fstr){
        try {
            if (fstr.contains("<hex:")) {
                byte[] inBytes = fstr.getBytes(StandardCharsets.UTF_8);
                byte[] outBytes = new byte[0];
                int len = inBytes.length;
                for(int i=0;i<len;i++){
                    if( inBytes[i] == (byte)0x3c
                            && inBytes[i+1]== (byte)0x68
                            && inBytes[i+2]== (byte)0x65
                            && inBytes[i+3]== (byte)0x78
                            && inBytes[i+4]== (byte)0x3a
                    ){
                        i+=5;
                        while(inBytes[i]!= (byte)0x3e){
                            byte[] tmp = {inBytes[i],inBytes[i+1]};
                            byte it = (byte)Integer.parseInt(new String(tmp), 16);
                            outBytes = ArrayUtil.append(outBytes,it);
                            i+=2;
                        }
                    }else{
                        outBytes = ArrayUtil.append(outBytes, inBytes[i]);
                    }
                }
                return outBytes;
            }else{
                return fstr.getBytes(StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            System.out.println(e);
            return fstr.getBytes();
        }
    }

    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("log.txt");
        String hex = "e59091e79baee6a899e68980e59ca8e696b9e59091e799bce587bae79bb4e7b79ae7af84e59c8de789a9e79086e694bbe6938ae38080024804f201f803024904f201f903e5a881e58a9befbc9a0249020103024802010338353002100103024804f201f803024904f201f903e799bce58b95e6a29de4bbb6efbc9a02490201030248020103024804f201f403024904f201f503e58a8de6b0a3024902010302480201033530e9bb9e020857e4e94523ff4f02084be0e94949ff4302100103e88887024804f201f403024904f201f503e5bf85e6aebae58a8dc2b7e99683e5bdb102490201030248020103e585b1e4baabe8a487e594b1e69982e99693ff0103ff0103";
        String fstr = FFXIVString.parseFFXIVString(HexUtils.hexStringToBytes(hex));
        fw.write(fstr+"\n");
        String fstr2 = "向目標所在方向發出直線範圍物理攻擊　<hex:024804F201F803><hex:024904F201F903>威力：<hex:0249020103><hex:0248020103>850<hex:02100103><hex:024804F201F803><hex:024904F201F903>發動條件：<hex:0249020103><hex:0248020103><hex:024804F201F403><hex:024904F201F503>劍氣<hex:0249020103><hex:0248020103>50點<hex:020857E4E94523FF4F02084BE0E94949FF4302100103E88887024804F201F403024904F201F503E5BF85E6AEBAE58A8DC2B7E99683E5BDB102490201030248020103E585B1E4BAABE8A487E594B1E69982E99693FF0103FF0103>";
        fw.write(fstr2+"\n");
        fw.write(FFXIVString.parseFFXIVString(FFXIVString.fstr2bytes(fstr2))+"\n");
        fw.write(HexUtils.bytesToHexString(HexUtils.hexStringToBytes(hex))+"\n");
        fw.write(HexUtils.bytesToHexString(FFXIVString.fstr2bytes(fstr2))+"\n");
        fw.write(HexUtils.bytesToHexString(FFXIVString.fstr2bytes(FFXIVString.parseFFXIVString(FFXIVString.fstr2bytes(fstr2))))+"\n");




        fw.close();
    }
}
