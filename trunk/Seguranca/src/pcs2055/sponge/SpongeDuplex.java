package pcs2055.sponge;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class SpongeDuplex implements Duplex {

    // c = b - r
    private int r; // bitrate
    private int c; // capacity
    private int b; // state size
    private byte[] s; // the sponge state
    private byte[] Z; // the Z output
    private TransformationF transf; // the transformation/permutation f 
    private int hashBits;
    
    @Override
    public void init(int hashBits) {
        
        b = c + r;
        s = new byte[b]; // s = 0^b
        Z = new byte[0];
        this.hashBits = hashBits;
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
        s = Arrays.copyOfRange(s, 0, hashBits); //  ⌊s⌋l

        // acumula no buffer de saída
        Z = ByteUtil.append(Z, s, Z.length, hashBits);

        // retorna z
        z = new byte[zLength];
        int max = 0;
        if (zLength < Z.length)
            max = zLength;
        else
            max = Z.length;
        for (int i=0; i<max; i++)
            z[i] = Z[i];
        
        return s;
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
