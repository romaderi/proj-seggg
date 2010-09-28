package pcs2055.test;

import pcs2055.curupira.*;
import pcs2055.math.ByteUtil;

public class CurupiraTest {
	
	public static void main(String[] args) {
	
		byte[] cypherKey = new byte[12];
		
		for (int i = 0; i < 12; i++)
			cypherKey[i] = 0x00;
		
		Curupira cur = new Curupira();
		cur.makeKey(cypherKey, 96);
		
		byte[] mBlock = new byte[12];
		byte[] cBlock = new byte[12];
		
		cur.encrypt(mBlock, cBlock);
		System.out.print("=>>>   ");
		ByteUtil.printArray(cBlock);
	}

}
