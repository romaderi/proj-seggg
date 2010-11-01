package pcs2055.sponge;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class SpongeDuplex implements Duplex {

    // c = b - r
    private int r; // bitrate
    private int c; // capacity
    private int b; // state size
    private byte[] s; // the sponge state
    private TransformationF transf; // the transformation/permutation f 
    
    @Override
    public void init(int hashBits) {
        
        // hashBits ?
        b = c + r;
        s = new byte[b]; // s = 0^b
    }

    @Override
    public int getBitRate() {
        
        return r;
    }
    
    @Override
    public int getCapacity() {
        
        return c;
    }

    @Override
    public byte[] duplexing(byte[] sigma, int sigmaLength, byte[] z, int zLength) {
        
        byte[] pad = this.pad101(sigma, sigmaLength);
        byte[] P = ByteUtil.append(sigma, pad, sigmaLength, r); // P = σ||pad[r](|σ|)
        
        byte[] zeros = new byte[c];
        P = ByteUtil.append(P, zeros, P.length, c);
        s = ByteUtil.xor(s, P, b); // s = s ⊕ (P ||0^b−r )
        s = transf.f(s); // s = f (s)
        
        // z
        
        return Arrays.copyOfRange(s, 0, zLength); //  ⌊s⌋l
    }

    /**
     * Minimal sponge padding
     * @param M
     * @return
     */
    private byte[] pad101(byte[] M, int mLength) {
        
        int q = (-mLength - 10) % r;
        byte[] pad = new byte[q/8];
        pad[0] = (byte) 0x80;
        pad[q/8] = (byte) (0x01 | pad[q/8]);
        return ByteUtil.append(M, pad, mLength, q/8);
    }


}
