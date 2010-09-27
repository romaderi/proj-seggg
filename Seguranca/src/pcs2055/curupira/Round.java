package pcs2055.curupira;

import pcs2055.math.ByteUtil;

public class Round {
        
    public static byte[] initialKeyAddition(byte[] block, byte[] subkey) {
        
        return null;
    }
    
    
    public static byte[] roundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = teta(block);
        block = sigma(block, subkey);
        return block;
    }
    
    public static byte[] lastRoundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = sigma(block, subkey);
        return block;
    }
    
    private static byte[] gama(byte[] a) {
        
    	return null;
    }
    
    private static byte[] pi(byte[] a) {
    	
    	int i, j, n;
    	n = 4;
    	byte[] result = a.clone();
    	
    	for (i = 1; i < 3; i++)
    		for (j = 0; j < n; j++)
    			result[i*n + j] = a[i*n + (i^j)];
        
    	return result;
    }
    
    private static byte[] teta(byte[] a) {

    	int n = 4;
    	byte[] MDS = {3, 2, 2, 4, 5, 4, 6, 6, 7};
        return ByteUtil.mult3xn(MDS, a, n);	
    }
    
    private static byte[] sigma(byte[] a, byte[] subkey) {
        
        return ByteUtil.xor(a, subkey, 12);
    }

}
