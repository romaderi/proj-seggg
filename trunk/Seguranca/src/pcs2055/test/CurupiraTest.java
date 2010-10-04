package pcs2055.test;

import pcs2055.curupira.*;

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
		byte[] cBlock = new byte[12];
		byte[] mBlock = new byte[12];
		
		for (int i = 0; i < 12; i++) {
			cypherKey[i] = (byte)0;
			mBlock[i] = (byte)0x00;
		}
		
		/*
		cypherKey[0] = (byte)0x23;
		cypherKey[1] = (byte)0x87;
		cypherKey[2] = (byte)0xf9;
		cypherKey[3] = (byte)0x64;
		cypherKey[4] = (byte)0x4f;
		cypherKey[5] = (byte)0xbb;
		cypherKey[6] = (byte)0x17;
		cypherKey[7] = (byte)0x86;
		cypherKey[8] = (byte)0x32;
		cypherKey[9] = (byte)0xa6;
		cypherKey[10] = (byte)0xb2;
		cypherKey[11] = (byte)0x0a;*/
		
		Curupira cur = new Curupira();
		cur.makeKey(cypherKey, 96);
		
		cur.encrypt(mBlock, cBlock);
		
		System.out.println("\n\n\n");
		
		cBlock[0] = (byte)0xb4;
		cBlock[1] = (byte)0x8c;
		cBlock[2] = (byte)0xbb;
		cBlock[3] = (byte)0x91;
		cBlock[4] = (byte)0x49;
		cBlock[5] = (byte)0x13;
		cBlock[6] = (byte)0x1c;
		cBlock[7] = (byte)0x39;
		cBlock[8] = (byte)0x99;
		cBlock[9] = (byte)0x5f;
		cBlock[10] = (byte)0xfb;
		cBlock[11] = (byte)0x3a;
		for (int j=0; j < 12; j++)
			mBlock[j] = (byte)0xFF;
		
		cur.decrypt(cBlock, mBlock);
		ByteUtil.printArray(mBlock);
		
		/*
		for (int i = 0; i < 12; i++)
			mBlock[i] = 0x00;
		
		cypherKey[0] = (byte) 0x23;
		cypherKey[1] = (byte) 0x87;
		cypherKey[2] = (byte) 0xf9;
		cypherKey[3] = (byte) 0x64;
		cypherKey[4] = (byte) 0x4f;
		cypherKey[5] = (byte) 0xbb;
		cypherKey[6] = (byte) 0x17;
		cypherKey[7] = (byte) 0x86;
		cypherKey[8] = (byte) 0x32;
		cypherKey[9] = (byte) 0xa6;
		cypherKey[10] = (byte) 0xb2;
		cypherKey[11] = (byte) 0x0a;
		
		ByteUtil.printArray(cypherKey);
		
		cur.makeKey(cypherKey, 96);
		
		cur.encrypt(mBlock, cBlock);
		
		for (int i = 0; i < 12; i++)
			mBlock[i] = (byte)0xFF;

		cur.decrypt(cBlock, mBlock);*/
	}
	
}
