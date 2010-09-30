package pcs2055.curupira;

import pcs2055.math.ByteUtil;
import pcs2055.curupira.SBox;

public class Round {
        
    public static byte[] initialKeyAddition(byte[] block, byte[] subkey) {
        
    	byte[] result = sigma(block, subkey);

    	System.out.print("INITIAL KEY ADDITION -> ");
    	ByteUtil.printArray(result);
    	
        return result;
    	//return null;
    }
    
    
    public static byte[] roundFunction(byte[] block, byte[] subkey) {
        
    	System.out.println("***ROUND BEGIN!!!!!");
        block = gama(block);
    	System.out.print(" GAMA -> ");
    	ByteUtil.printArray(block);
        block = pi(block);
    	System.out.print(" PI -> ");
    	ByteUtil.printArray(block);
        block = teta(block);
    	System.out.print(" TETA -> ");
    	ByteUtil.printArray(block);
        block = sigma(block, subkey);
    	System.out.print(" SIGMA -> ");
    	ByteUtil.printArray(block);
        return block;
    }
    
    public static byte[] lastRoundFunction(byte[] block, byte[] subkey) {
    	
    	System.out.println("+++++ LAST ROUND BEGIN");
        block = gama(block);
    	System.out.print(" GAMA -> ");
    	ByteUtil.printArray(block);
        block = pi(block);
    	System.out.print(" PI -> ");
    	ByteUtil.printArray(block);
        block = sigma(block, subkey);
    	System.out.print(" SIGMA -> ");
    	ByteUtil.printArray(block);
        return block;
    }
    
    private static byte[] gama(byte[] a) {
        
    	byte[] b = new byte[12];
    	int i;
    	
    	for (i = 0; i < 12; i++)
    		b[i] = SBox.sbox16b(a[i]);

    	return b;
    }
    
    private static byte[] pi(byte[] a) {
    	
    	int i, j, n;
    	n = 4;
    	byte[] result = a.clone();
    	
    	for (i = 1; i < 3; i++)
    		for (j = 0; j < n; j++)
    			result[i + 3*j] = a[i + 3*(i^j)];
    	
    	return result;
    }
    
    private static byte[] alg2 (byte[] a){
    	
    	byte v = ByteUtil.xtimes((byte)(a[0] ^ a[1] ^ a[2]));
    	byte w = ByteUtil.xtimes(v);
    	byte[] b = new byte[3];
    	b[0] = (byte)(a[0] ^ v);
    	b[1] = (byte)(a[1] ^ w);
    	b[2] = (byte)(a[2] ^ v ^ w);
    	return b;
    }
    
    private static byte[] teta(byte[] a) {

    	byte[] b0 = new byte[3];
    	byte[] b = new byte[12];

    	for (int j = 0; j < 4; j++) {
    		b0[0] = a[3*j];
    		b0[1] = a[3*j + 1];
    		b0[2] = a[3*j + 2];
    		b0 = alg2(b0);
    		b[3*j] = b0[0];
    		b[3*j + 1] = b0[1];
    		b[3*j + 2] = b0[2];
    	}
    	
    	return b;
    	/*
    	int n = 4;
    	byte[] MDS = {3, 4, 6, 2, 5, 6, 2, 4, 7};
        return ByteUtil.mult3xn(MDS, a, n);*/
    }
    
    public static byte[] sigma(byte[] a, byte[] subkey) {
        
    	return ByteUtil.xor(a, subkey, 12);
    }

}