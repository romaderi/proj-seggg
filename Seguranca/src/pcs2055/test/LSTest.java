package pcs2055.test;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.ls.LetterSoup;
import pcs2055.marvin.Marvin;
import pcs2055.math.ByteUtil;

public class LSTest {

    public static void main(String[] args) {

        byte[] msg = new byte[23];
        byte[] key = new byte[12];
        byte[] nounce = new byte[12];
        byte[] header = new byte[] {0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
        nounce[11] = 0x2A;
        
        System.out.println("== 1o teste ==");
        
        test(msg, key, null, nounce, 12);

        test(msg, key, header, nounce, 12);

        ///////////////////////////
        
        System.out.println("\n== #0-0 Set 1 - key = 0, msg = 0, add = null, nounce = 0 ==");
        
        nounce = new byte[12];
        msg = new byte[1];
        test3(msg, key, null, nounce);        
        

    }
    
    private static void test3(byte[] plainText, byte[] key, byte[] header, byte[] nounce) {
    
       test(plainText, key, header, nounce, 4);
       test(plainText, key, header, nounce, 8);
       test(plainText, key, header, nounce, 12);
    }    
    
    private static void test(byte[] plainText, byte[] key, byte[] header, byte[] nounce, int tagbytes) {
        
        BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
        
        ls.setKey(key, Curupira.KEY_SIZE_96);
        ls.setCipher(curupira);
        ls.setIV(nounce, nounce.length);
        ls.setMAC(marvin);

        ByteUtil.printArray(key, "key= ");
        ByteUtil.printArray(nounce, "nounce= ");
        ByteUtil.printArray(plainText, "msg= ");

        byte[] cData = new byte[12]; // inútil aqui
        byte[] crypto = ls.encrypt(plainText, plainText.length, cData);
        
        byte[] tag = ls.getTag(cData, tagbytes*8);
        
        if (header != null) {
            
            ByteUtil.printArray(header, "add= ");
            ls.update(header, header.length);
            tag = ls.getTag(header, tagbytes*8);
        }

        ByteUtil.printArray(crypto, "Ciphertext= ");
        ByteUtil.printArray(tag, "Tag("+tagbytes+")= ");

        byte[] mData = new byte[plainText.length]; // inútil aqui
        byte[] decrypt = ls.decrypt(crypto, plainText.length, mData);
        
        ByteUtil.printArray(decrypt, "PlainText= ");
        System.out.println("");
    }
    
}
