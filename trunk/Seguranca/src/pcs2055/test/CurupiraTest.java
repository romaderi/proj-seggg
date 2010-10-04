package pcs2055.test;

import pcs2055.curupira.*;

import pcs2055.curupira.*;
import pcs2055.math.ByteUtil;

public class CurupiraTest {
	
 	public static void main(String[] args) {
 		
		byte[] cypherKey = new byte[12];
		byte[] cBlock = new byte[12];
		byte[] mBlock = new byte[12];
		
		for (int i = 0; i < 12; i++) {
			cypherKey[i] = (byte)0x00;
			mBlock[i] = (byte)0x00;
		}
		cypherKey[5] = (byte) 0x00;
		
		Curupira cur = new Curupira();
		cur.makeKey(cypherKey, 96);
	
		
		mBlock = new byte[] { (byte) 0xc8, 0x14, (byte) 0xef, 
		        0x73, 0x05, 0x26, 
		        (byte) 0xd3, (byte) 0xa9, (byte) 0xf9,
		        (byte) 0xbc,  0x44   , (byte) 0xce
		};
		                  
		cur.encrypt(mBlock, cBlock);
			
		System.out.print("PLEIN TEXT : ");
		ByteUtil.printArray(mBlock);
		System.out.print("CYPHER KEY : ");
		ByteUtil.printArray(cypherKey);
		System.out.print("CYPHERTEXT : ");
		ByteUtil.printArray(cBlock);

		for (int j=0; j < 12; j++)
			mBlock[j] = (byte)0xFF;
		
		cur.decrypt(cBlock, mBlock);

		System.out.print("CYPHERTEXT : ");
		ByteUtil.printArray(cBlock);
		System.out.print("CYPHER KEY : ");
		ByteUtil.printArray(cypherKey);
		System.out.print("PLEIN TEXT : ");
		ByteUtil.printArray(mBlock);

	}
	
}
