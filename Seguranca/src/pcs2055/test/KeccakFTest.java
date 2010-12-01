package pcs2055.test;

import pcs2055.hash.Keccak;
import pcs2055.hash.KeccakF;
import pcs2055.math.ByteUtil;

public class KeccakFTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
				0x5C411EBBCE32C240L,0xB90476D307CE802EL,
				0xC82C1C49BC1BEC48L,0xC0675EC2A6C6F3EDL,
				0x3E5B741D13437095L,0x707C565E10D8A20BL,
				0x8C20468FF9514FCFL,0x31B4249CD82DCEE5L,
				0x8C0A2AF538B291A8L,0x7E3390D737191A07L,
				0x484A5D3F3FB8C8F1L,0x5CE056E5E5F8FEBEL,
				0x5E1FB59D6740980AL,0xA06CA8A0C20F5712L,
				0xB4CDE5D032E92AB8L};
//		byte[] m = ByteUtil.longArrayToByteArray(M);
//		byte[] tmp = new byte[]{(byte)0x9F, (byte)0x0A, (byte)0xE1};
//		m = ByteUtil.append(m, tmp, m.length, tmp.length);
//
//		k.update(m, m.length);
//		byte[] Z = k.getHash(null);	
//		ByteUtil.printArray(Z);
	

		k.init(0);
		
		long[] test = new long[]{
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

//		k.update(ByteUtil.longArrayToByteArray(test), test.length*8);
//		Z = k.getHash(null);	
//		ByteUtil.printArray(Z);
		
//		long[] msg = new long[]{
//				0x1EED9CBA179A009EL,0xC2EC5508773DD305L,
//				0x477CA117E6D569E6L,0x6B5F64C6BC64801CL,
//				0xE25A8424CE4A26D5L,0x75B8A6FB10EAD3FDL,
//				0x1992EDDDEEC2EBE7L,0x150DC98F63ADC323L,
//				0x7EF57B91397AA8A7L		
//		};
		
		byte[] msg = new byte[]{'A'};
//				0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
//				0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13,
//				0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D,
//				0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27};
//		msg = new byte[390];
		Keccak K = new Keccak();
		K.init(0);
		K.update(msg, msg.length);
		ByteUtil.printArray(K.getHash(null));
//		KeccakF kf = new KeccakF();
//		long[] blank = new long[25];
//		ByteUtil.printArray(kf.f(ByteUtil.longArrayToByteArray(blank)));
//		ByteUtil.printArray(kf.f(kf.f(ByteUtil.longArrayToByteArray(blank))), "\n");
		
		
	}

}
