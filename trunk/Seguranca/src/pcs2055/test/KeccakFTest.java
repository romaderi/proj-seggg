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
		
		Keccak k = new Keccak();
		
		k.init(0);
		
		long[] M = new long[]{
				0x83AF34279CCB5430L,0xFEBEC07A81950D30L,
				0xF4B66F484826AFEEL,0x7456F0071A51E1BBL,
				0xC55570B5CC7EC6F9L,0x309C17BF5BEFDD7CL,
				0x6BA6E968CF218A2BL,0x34BD5CF927AB846EL,
				0x38A40BBD81759E9EL,0x33381016A755F699L,
				0xDF35D660007B5EADL,0xF292FEEFB735207EL,
				0xBF70B5BD17834F7BL,0xFA0E16CB219AD4AFL,
				0x524AB1EA37334AA6L,0x6435E5D397FC0A06L,
				0x0000000000000000L,0x0000000000000000L,
				0x0000000000000000L,0x0000000000000000L,
				0x0000000000000000L,0x0000000000000000L,
				0x0000000000000000L,0x0000000000000000L,
				0x0000000000000000L};
		
		k.update(ByteUtil.longArrayToByteArray(M), M.length*8);
		byte[] Z = k.getHash(null);
		
		ByteUtil.printArray(Z);
		
	}

}
