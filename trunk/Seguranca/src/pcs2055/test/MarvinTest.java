package pcs2055.test;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.marvin.Marvin;

public class MarvinTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        
        // 12 bytes
        byte[] key = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 
                      0x0, 0x0, 0x0, 0x0, 0x0, 0x0}; 

        byte[] input = {0x0,  0x0,  0x0, 0x0,   0x0,  0x0, 
                        0x0,  0x0,  0x0, 0x0,   0x0,  0x1,  
                        0x2,  0x3, 0x4,   0x5,  0x6,  0x7,  
                        0x8,  0x9, 0xA,   0xB,  0xC,  0xD,  
                        0xE,  0xF, 0x10,  0x11, 0x12, 0x13, 
                        0x14, 0x15, 0x16, 0x17};

    
        BlockCipher curupira = new Curupira();
        
        MAC marvin = new Marvin();
        marvin.setKey(key, Curupira.KEY_SIZE_96);
        marvin.setCipher(curupira);
        
        marvin.init();
        
    }

}
