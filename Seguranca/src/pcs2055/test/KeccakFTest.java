package pcs2055.test;

import pcs2055.hash.Keccak;
import pcs2055.hash.KeccakF;
import pcs2055.math.ByteUtil;

public class KeccakFTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		byte[] aa = new byte[1];
		aa[0] = 0x7A;
		int d = 0;
		int r = 1024;
		

		long[] data = new long[25];
    	data[0] = 0x7A01000000000000L;
    	data[16] = 0x80L;
		long[] a = KeccakF.f(data);
		ByteUtil.printArray(a);
		
	}

}
