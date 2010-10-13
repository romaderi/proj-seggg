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
        
        System.out.println("** 1o teste **");
        test(msg, key, null, nounce);
        test(msg, key, header, nounce);

        ///////////////////////////
        
        System.out.println("\n** #0-0 Set 1 - key = 0, msg = 0, add = null, nounce = 0 **");
        
        nounce = new byte[12];
        msg = new byte[1];
        test(msg, key, null, nounce);        
        msg = new byte[7];
        test(msg, key, null, nounce);        
        msg = new byte[8];
        test(msg, key, null, nounce);      
        msg = new byte[9];
        test(msg, key, null, nounce);        
        msg = new byte[11];
        test(msg, key, null, nounce);        
        msg = new byte[12];
        test(msg, key, null, nounce);        
        msg = new byte[13];
        test(msg, key, null, nounce);        

        ///////////////////////////

        System.out.println("\n** #0-0 Set 2 - key = 0, msg != 0, add = null, nounce = 0 **");
        
        msg = new byte[]{1};
        test(msg, key, null, nounce);        
        msg = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        test(msg, key, null, nounce);        
        msg = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        test(msg, key, null, nounce);        
        msg = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0xa, 0xb, 0xc};
        test(msg, key, null, nounce);        
        msg = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0xa, 0xb, 0xc, 0xd};
        test(msg, key, null, nounce);  
        
        System.out.println("\n** #0-0 Set 3 - key = 0, msg = 0, add = 0, nounce = 0 **");
        
        msg = new byte[1];
        header = new byte[1];
        test(msg, key, header, nounce);        

//        System.out.println("\n** #0-0 Set 4 - key = 0, msg != 0, add != 0, nounce = 0 **");
//
//        msg = new byte[] {1};
//        header = new byte[] {1};
//        test(msg, key, header, nounce);        

        System.out.println("\n** #0-1 Set 1 - key = 0, msg = 0, add = null, nounce != 0 **");

        msg = new byte[1];
        nounce = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};
        test(msg, key, null, nounce);        

    }
        
    private static void test(byte[] plainText, byte[] key, byte[] header, byte[] nounce) {
        
        // input
        
        ByteUtil.printArray(key, "key= ");
        ByteUtil.printArray(nounce, "nounce= ");
        ByteUtil.printArray(plainText, "msg("+ plainText.length + ")= ");
        
        // init
        
        BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
        
        ls.setKey(key, Curupira.KEY_SIZE_96);
        ls.setCipher(curupira);
        ls.setIV(nounce, nounce.length);
        ls.setMAC(marvin);

        // encrypt
        
        byte[] cData = new byte[12]; // inútil aqui
        byte[] crypto = ls.encrypt(plainText, plainText.length, cData);
        
        // authenticate
        
        if (header != null) {
            
            ByteUtil.printArray(header, "add= ");
            ls.update(header, header.length);
        }
        
        byte[] tag4 = ls.getTag(cData, 4*8);
        byte[] tag8 = ls.getTag(cData, 8*8);
        byte[] tag12 = ls.getTag(cData, 12*8);

        // decrypt

        byte[] mData = new byte[plainText.length]; // inútil aqui
        byte[] decrypt = ls.decrypt(crypto, plainText.length, mData);

        // output
        
        ByteUtil.printArray(crypto, "Ciphertext= ");
        ByteUtil.printArray(decrypt, "PlainText= ");
        ByteUtil.printArray(tag4, "Tag(4)= ");
        ByteUtil.printArray(tag8, "Tag(8)= ");
        ByteUtil.printArray(tag12, "Tag(12)= ");
        System.out.println("");
    }
    
}
