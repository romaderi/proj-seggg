package pcs2055.test;

import java.math.BigInteger;

import pcs2055.curupira.*;
import pcs2055.math.ByteUtil;

public class CurupiraTest {
	
 	public static void main(String[] args) {
	
 		/*
 		byte[] key = {(byte)0x8b, (byte)0x13, (byte)0x13, (byte)0x4d, (byte)0x53,
 				(byte)0x53, (byte)0x30, (byte)0xe3, (byte)0xe3, (byte)0x0c,
 				(byte)0x04, (byte)0x04};
 		byte[] txt = {0, (byte)0xba, (byte)0xba, 0, (byte)0xba, (byte)0xba,
 				0, (byte)0xba, (byte)0xba, 0, (byte)0xba, (byte)0xba}; 
 		
 		ByteUtil.printArray(Round.sigma(key, txt)); 		
 		*/
 		
		byte[] cypherKey = new byte[12];
		
		for (int i = 0; i < 12; i++)
			cypherKey[i] = (byte)0;
		
		Curupira cur = new Curupira();
		cur.makeKey(cypherKey, 96);
		
		byte[] mBlock = new byte[12];
		for (int i = 0; i < 12; i++)
			mBlock[i] = 0x00;
		byte[] cBlock = new byte[12];
		
		cur.encrypt(mBlock, cBlock);
		System.out.print("=>>>   ");
		ByteUtil.printArray(cBlock);
	}
	
}
