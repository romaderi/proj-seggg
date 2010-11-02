package pcs2055.hash;

import java.math.BigInteger;
import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class Keccak implements HashFunction {

	private int b = 1600;
	private int l = 6; // b = 25*(2^l)
	private int r = 1024; // default value
	private int c = b - r; // default value = 526
	private int d = 0; // default value
    private long[] S;
	private int hashbitlen;
    private long[] P;
	
    @Override
    public void init(int hashBits) {
        // TODO Auto-generated method stub
        this.hashbitlen = hashBits;
    	this.S = new long[25];
        this.P = new long[25];
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
    	
    	//if ( aLength != )
    	//byte[] tmpP = new byte[];
    	
    	
        
    }
    
    @Override
    public byte[] getHash(byte[] val) {
        // TODO Auto-generated method stub
    	
        return null;
    }
    
    private static byte[] pad(byte[] M, int n){
    	
    	byte[] ret = new byte[M.length + 1];
    	byte[] bit1 = new byte[]{0x01};
    	
    	ret = ByteUtil.append(M, bit1, M.length, 1);
    	
    	if ( (ret.length*8) % n != 0 ) {
    		byte[] tmp = Arrays.copyOf(ret, ret.length);
    		byte[] bytes0 = new byte[(n-((tmp.length*8)%n))/8];
    		ret = new byte[ret.length + (n-((tmp.length*8)%n))/8];
    		ret = ByteUtil.append(tmp, bytes0, tmp.length, bytes0.length);
    	}
    	
    	return ret;
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
