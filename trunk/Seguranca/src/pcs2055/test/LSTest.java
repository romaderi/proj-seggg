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

        byte[] plainText = new byte[23];
        byte[] key = new byte[12];
        byte[] nounce = new byte[12];
        nounce[11] = 0x2A;
        
        test(plainText, key, null, nounce, Curupira.KEY_SIZE_96);
        
        ///////////////////////////
        
        byte[] header = new byte[] {0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
        plainText = new byte[23];
        key = new byte[12];
        nounce = new byte[12];
        nounce[11] = 0x2A;
        
        test(plainText, key, header, nounce, Curupira.KEY_SIZE_96);

        ///////////////////////////
        
        header = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        plainText = new byte[1];
        key = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};
        nounce = new byte[12];
        nounce[11] = 0x2A;
        
        test2(plainText, key, header, nounce, Curupira.KEY_SIZE_96);

    }
    
    private static void test(byte[] plainText, byte[] key, byte[] header, byte[] nounce, int keybits) {
        
        BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
        
        ls.setKey(key, keybits);
        ls.setCipher(curupira);
        ls.setIV(nounce, 12);
        ls.setMAC(marvin);

        byte[] cData = new byte[12]; // inútil aqui
        byte[] crypto = ls.encrypt(plainText, 23, cData);
        
        ByteUtil.printArray(crypto, "Ciphertext= ");
        
        byte[] tag = ls.getTag(cData, 12*8);
        
        if (header != null) {
            ls.update(header, 23);
            tag = ls.getTag(header, 12*8);
        }

        ByteUtil.printArray(tag, "Tag= ");
        System.out.println("");
    }

    private static void test2(byte[] plainText, byte[] key, byte[] header, byte[] nounce, int keybits) {
        
        BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
        
        ls.setKey(key, keybits);
        ls.setCipher(curupira);
        ls.setIV(nounce, 12);
        ls.setMAC(marvin);

        byte[] cData = new byte[12]; // inútil aqui
        byte[] crypto = ls.encrypt(plainText, 23, cData);
        
        ByteUtil.printArray(crypto, "Ciphertext= ");
        
        byte[] tag = ls.getTag(cData, 12*8);
        
        if (header != null) {
            ls.update(header, 23);
            tag = ls.getTag(header, 12*8);
        }

        ByteUtil.printArray(tag, "Tag= ");
        System.out.println("");
    }
    
}
