package pcs2055.test;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.marvin.Marvin;
import pcs2055.math.ByteUtil;

public class MarvinTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        
        // 12 bytes
        byte[] key = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 
                      0x0, 0x0, 0x0, 0x0, 0x0, 0x0}; 

        byte[] chunk1 = {0x0,  0x0,  0x0, 0x0,   0x0,  0x0, 
                         0x0,  0x0,  0x0, 0x0,   0x0,  0x0};  
        byte[] chunk2 = {0x0,  0x0,  0x0, 0x0,   0x0,  0x0,  
                         0x0,  0x0,  0x0, 0x0,   0x0,  0x1};  
        byte[] chunk3 = {0x2,  0x3, 0x4,   0x5,  0x6,  0x7,  
                         0x8,  0x9, 0xA,   0xB,  0xC,  0xD};  
        byte[] chunk4 = {0xE,  0xF, 0x10,  0x11, 0x12, 0x13, 
                         0x14, 0x15, 0x16, 0x17};

    
        BlockCipher curupira = new Curupira();        
        MAC marvin = new Marvin();
        marvin.setKey(key, Curupira.KEY_SIZE_96);
        marvin.setCipher(curupira);
        
        marvin.init();
        marvin.update(chunk1, 12);
        marvin.update(chunk2, 12);
        marvin.update(chunk3, 12);
        marvin.update(chunk4, 10);
        byte[] tagFake = new byte[12];
        byte[] tag = marvin.getTag(tagFake, 96);
        ByteUtil.printArray(tag, "tag= ");
        System.out.println("");
        
//        key = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};
//        byte[] chunk = new byte[] {};
        
        byte[] M = new byte[]{0};
        test(marvin, key, M);

        M = new byte[]{0, 0};
        test(marvin, key, M);

        M = new byte[]{0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0};
        test(marvin, key, M);
    }
    
    public static void test(MAC marvin, byte[] key, byte[] M) {
        
        BlockCipher curupira = new Curupira();        
        marvin.setKey(key, Curupira.KEY_SIZE_96);
        marvin.setCipher(curupira);

        ByteUtil.printArray(M, "Testando ");
        System.out.println("");
        marvin.init();
        marvin.update(M, M.length);
        byte[] tagFake = new byte[12];
        byte[] tag = marvin.getTag(tagFake, 4*8);
        ByteUtil.printArray(tag, "tag(04)= ");
        tag = marvin.getTag(tagFake, 8*8);
        ByteUtil.printArray(tag, "tag(08)= ");
        tag = marvin.getTag(tagFake, 12*8);
        ByteUtil.printArray(tag, "tag(12)= ");
        System.out.println("");
    }

}
