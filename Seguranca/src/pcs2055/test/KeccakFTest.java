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
    	data[0] = 0x000000000000017AL;
    	data[15] = 0x8000000000000000L;
    	//data[1] = 0x7A01000000000080L;
    	//data[4] = 0xF502000000000000L;
    	/*data[5] = 0x7A01000000000080L;
    	data[9] = 0xF502000000000000L;
    	data[11] = 0x7A01000000000080L;
    	data[14] = 0xF502000000000000L;
    	data[16] = 0x7A01000000000080L;
    	data[19] = 0xF502000000000000L;
    	data[21] = 0x7A01000000000080L;
    	data[24] = 0xF502000000000000L;*/
    	
//    	ByteUtil.printArray(data);
//    	System.out.println();
//    	ByteUtil.printArray(ByteUtil.longArrayToByteArray(data));
//    	System.out.println();
//    	ByteUtil.printArray(ByteUtil.byteArrayToLongArray(ByteUtil.longArrayToByteArray(data)));
//    	
    	//TODO: mais testes do byte->long e vice-versa
    	
    	KeccakF kf = new KeccakF();
    	byte[] a = kf.f(ByteUtil.longArrayToByteArray(data));
		data = ByteUtil.byteArrayToLongArray(a);
		ByteUtil.printArray(data);
		
//		System.out.println();
//		a = kf.f(ByteUtil.longArrayToByteArray(data));
//		data = ByteUtil.byteArrayToLongArray(a);
//		ByteUtil.printArray(ByteUtil.invertLongArray(data));
		
	}

}
