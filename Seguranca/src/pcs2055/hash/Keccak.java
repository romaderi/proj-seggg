package pcs2055.hash;

import java.util.Arrays;

import pcs2055.math.ByteUtil;
import pcs2055.sponge.Duplex;

public class Keccak implements HashFunction, Duplex {

    private final int b = 1600/8; // state size, em bytes
    private int r; // bitrate (tamanho do estado que é xorado com a entrada)
    private int c; // capacity // c = b - r (quanto maior c, maior a segurança)
    private int d;
    // obs: aqui r, c, b estão em bytes
    private byte[] s; // the sponge state
    private byte[] Z; // the Z output   
    private byte[] buffer;
    
    KeccakF keccakF = new KeccakF();
	
    @Override
    public void init(int hashBits) {
        
        // Via de regra, c = 2*hashBits 
        // Só com os parâmetros default isso não ocorre.
        // by @pbarreto

    	if ( hashBits == 0){
    		c = 576/8;
    		r = 1024/8;
    		d = 0;
    	} else {
	        c = 2 * hashBits/8;
	        r = b - c;
	        d = 0;
    	}
        s = new byte[b]; // s = 0^b
        Z = new byte[0];
        buffer = new byte[0];
    }
    
    public void setCapacity(int capacity){
    	this.c = capacity/8;
    }

    @Override
    public void update(byte[] aData, int aLength) {
        
    	buffer = ByteUtil.append(buffer, aData, buffer.length, aLength);
    	byte[] pi = new byte[this.r];
    	byte[] sr = new byte[this.r];    	
    	
    	if ( buffer.length < this.r)
    		return;
    	
    	while ( buffer.length >= this.r ){
    		pi = Arrays.copyOf(buffer, this.r);
    		buffer = Arrays.copyOfRange(buffer, this.r, buffer.length);
    		this.duplexing(pi, this.r, sr, sr.length);
    	}
    	
    }
    
    @Override
    public byte[] getHash(byte[] val) {

		byte[] sr = new byte[this.r];
		
		byte[] app = new byte[]{(byte)0x01, this.enc(this.d*8), this.enc(this.r)}; // pad(M,8)||enc(d)||enc(r/8)
		buffer = ByteUtil.append(buffer, app, buffer.length, app.length);
		buffer = this.pad(buffer, this.r); // pad(M,r)
		
		byte[] pi;
		while ( buffer.length >= this.r ){
			pi = Arrays.copyOf(buffer, this.r);
    		buffer = Arrays.copyOfRange(buffer, this.r, buffer.length);
    		this.duplexing(pi, this.r, sr, sr.length);
    	}
		Z = ByteUtil.append(Z, sr, Z.length, sr.length);
		buffer = new byte[0];
		
		if ( val == null )
			val = new byte[Z.length];
	    val = Arrays.copyOf(Z, c/2);
	    
        return Arrays.copyOf(Z, c/2);
    }
    
    private byte[] pad(byte[] M, int n){
    	
    	byte[] ret = new byte[M.length + 1];
    	byte[] bit1 = new byte[]{(byte)0x01};
    	
    	ret = ByteUtil.append(M, bit1, M.length, 1);
    	
    	if ( ret.length % n != 0 ) {
    		byte[] tmp = Arrays.copyOf(ret, ret.length);
    		byte[] bytes0 = new byte[n-(tmp.length%n)];
    		ret = new byte[tmp.length + n-(tmp.length%n)];
    		ret = ByteUtil.append(tmp, bytes0, tmp.length, bytes0.length);
    	}
    	
    	return ret;
    }

    public byte enc(int x){
    	return (byte)x;
    }

    /*************/
    /*  Duplex  */
    /***********/
    
    @Override
    public byte[] duplexing(byte[] sigma, int sigmaLength, byte[] z, int zLength) {

        // Z = D.duplexing(σ, l) with l ≤ r and Z ∈ Zl²
        
        if (sigmaLength > r)
            return null; // TODO: seria melhor se fosse uma exception

        // P = σ||pad[r](|σ|)
        byte[] P = Arrays.copyOf(sigma, sigmaLength);
        if (sigmaLength < r) {
            byte[] pad = this.pad101(sigmaLength);
            if (sigmaLength + pad.length > r)
                return null; // TODO: seria melhor se fosse uma exception
            P = ByteUtil.append(P, pad, sigmaLength, pad.length);
        }

        byte[] zeros = new byte[c]; // c = b - r

        P = ByteUtil.append(P, zeros, P.length, c); // P.length < r (não devia ser igual a r?!)
        s = ByteUtil.xor(s, P, b); // s = s ⊕ (P ||0^b−r )
        s = keccakF.f(s); // s = f (s)
        
        if (zLength > r)
            return null; // TODO: seria melhor se fosse uma exception
        if (z == null)
            z = new byte[zLength];
        for (int i=0; i<zLength; i++) //  ⌊s⌋l;
            z[i] = s[i];
        return z;
    }

    @Override
    public int getBitRate() {
        return r;
    }

    @Override
    public int getCapacity() {
        return c;
    }

    /**
     * Minimal sponge padding (10*1)
     * 
     * @param mlength comprimento em bytes da mensagem do padding
     * @return o fim do padding que deve ser concatenado com o resto da mensagem
     */
    private byte[] pad101(int mLength) {
        
        int q = r-mLength;
        byte[] pad = new byte[q];
        pad[q-1] = (byte) 0x80; // nessa linha devia ser 0
        pad[0] = (byte) (0x01 | pad[0]); // e aqui q-1
        // ?????
        
        return pad;
    }
}
