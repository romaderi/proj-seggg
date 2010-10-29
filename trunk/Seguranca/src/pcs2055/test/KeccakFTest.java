package pcs2055.test;

import pcs2055.hash.KeccakF;
import pcs2055.math.ByteUtil;

public class KeccakFTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long[] data = new long[25];
    	data[24] = 0x7A01000000000000L;
    	data[9] = 0x80L;
		long[] a = KeccakF.f(data);
		ByteUtil.printArray(a);
		
	}

}
