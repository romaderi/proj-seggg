package pcs2055.test;

import pcs2055.curupira.Round;
import pcs2055.curupira.SBox;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

public class mathTest {
    
    public static void main(String[] args) {

    	
    	//System.out.print(Integer.toHexString((short)((SBox.sbox16b((byte)0x07))&0xFF)) + " ");
    	
//    	for (int i = 0; i < 16; i++) { 
//    		for (int j = 0; j < 16; j++)
//    			System.out.print(Integer.toHexString((short)((SBox.sbox16b((byte)(i*16+j)))&0xFF)) + " ");
//    		System.out.println();
//    	}
    	
        //byte[] a = {0x0A}; 
        //byte[] b = {(byte) 0xAA}; 

        //byte c = MathUtil.T0((byte)0xAA);
        //System.out.printf("%x", c);
        
//        byte[] b = {10, 11, 0xC, 0xD, 0xE, 0xF, 0x10, 0x11, 0x12};
//        ByteUtil.print3xn(b, 9);
        
        int n = 0x0;
        String s = ByteUtil.bin(n);
        System.out.println(s);
        byte[] b = ByteUtil.binConcat1(n);
        ByteUtil.printArray(b);
    }

}
