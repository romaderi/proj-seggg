package pcs2055.hash;

import java.math.BigInteger;

import pcs2055.math.ByteUtil;

public class Keccak implements HashFunction {

	private int b = 1600;
	private int l = 6; // b = 25*(2^l)
	private int r = 1024; // default value
	private int c = b - r; // default value = 526
	private int d = 0; // default value
    private long[] S;
	private int hashbitlen;
    
    @Override
    public void init(int hashBits) {
        // TODO Auto-generated method stub
        this.hashbitlen = hashBits;
    	this.S = new long[25];
        
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public byte[] getHash(byte[] val) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private byte[] pad(byte[] M, int n){
    	return null;
    }
    
    private static byte[] enc(int x, int n){
    	
    	BigInteger y = BigInteger.valueOf((int)x);
    	BigInteger temp = BigInteger.ZERO;
    	
    	for (int i = 0; i < n; i++)
    		if ( y.testBit(i) )
    			temp = temp.setBit(n-i-1);

    	return temp.toByteArray();
    }

}
