package pcs2055.test;

import pcs2055.curupira.Round;
import pcs2055.math.ByteUtil;
import pcs2055.math.MathUtil;

public class mathTest {
    
    public static void main(String[] args) {

        byte[] a = {0x0A}; 
        byte[] b = {(byte) 0xAA}; 

        byte c = MathUtil.T0((byte)0xAA);
        System.out.printf("%x", c);
    }

}
