package pcs2055.blockCipher;

import pcs2055.math.ByteUtil;
import pcs2055.blockCipher.SBox;

public class Round {
        
    public static byte[] initialKeyAddition(byte[] block, byte[] subkey) {
    	
    	//byte[] result = sigma(block, subkey);

        return sigma(block, subkey);
    }
    
    
    public static byte[] roundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = teta(block);
        //block = sigma(block, subkey);
        return sigma(block, subkey);
    }
    
    public static byte[] lastRoundFunction(byte[] block, byte[] subkey) {
    	
        block = gama(block);
        block = pi(block);
        //block = sigma(block, subkey);
        return sigma(block, subkey);
    }
    
    public static byte[] sct(byte[] b) {

        // SCT é um nome emo pra aplicar (θ o π o γ) um certo número de vezes, 
        // sem σ[κ] (portanto, sem chave).
        // by @pbarreto

        byte[] gama = gama(b);
        byte[] pi = pi(gama);
        return teta(pi);
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
    }
    
    public static byte[] sigma(byte[] a, byte[] subkey) {
        
    	return ByteUtil.xor(a, subkey, a.length);
    }

}
