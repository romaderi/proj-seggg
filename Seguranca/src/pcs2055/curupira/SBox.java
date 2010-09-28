package pcs2055.curupira;

public class SBox {
    
    // minibox's
    public static final byte[] P = {0x3, 0xF, 0xE, 0x0, 0x5, 0x4, 0xB, 0xC, 0xD, 0xA, 0x9, 0x6, 0x7, 0x8, 0x2, 0x1}; 
    public static final byte[] Q = {0x9, 0xE, 0x5, 0x6, 0xA, 0x2, 0x3, 0xC, 0xF, 0x0, 0x4, 0xF, 0x7, 0xB, 0x1, 0x8}; 

    public static byte sbox16b(byte a) {
    	
    	byte u = a;
    	byte uh = 0x00, uh2 = 0x00;
    	byte ul = 0x00, ul2 = 0x00;

    	uh = P[(u >>> 4) & 0x0F];
    	ul = Q[u & 0x0F];
    	System.out.println("u = " + u + "   ; uh = " + uh);
    	System.out.println("u = " + u + "   ; ul = " + ul);
    	uh2 = Q[(uh & 0x0C)^((ul >>> 2) & 0x03)];
		ul2 = P[((uh << 2) & 0x0C)^(ul & 0x03)];
		System.out.println("uh2 = " + uh2);
    	System.out.println("ul2 = " + ul2);
		uh = P[(uh2 & 0x0C)^((ul2 >>> 2) & 0x03)];
		ul = Q[((uh2 << 2) & 0x0C)^(ul2 & 0x03)];
		
		System.out.println("uh = " + uh);
    	System.out.println("ul = " + ul);

		return (byte)((uh << 4)^ul);
    }
    
}
