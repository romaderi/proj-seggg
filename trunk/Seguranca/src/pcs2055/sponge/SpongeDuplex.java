package pcs2055.sponge;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

/**
 * usar o Keccak
 *
 */
@Deprecated
public class SpongeDuplex implements Duplex {

    private int r; // bitrate (tamanho do estado que é xorado com a entrada)
    private int c; // capacity // c = b - r (quanto maior c, maior a segurança)
    private int b; // state size
    // obs: aqui r, c, b estão em bytes
    private byte[] s; // the sponge state
    private byte[] Z; // the Z output
    private TransformationF transf; // the transformation/permutation f 
    private int hashBits; // = l: sponge returns the first l bits of the state at the output
    
    @Override
    public void init(int hashBits) {
        
        r = 1024; // TODO: 1024 bytes ou bits?
        c = 576;
        b = c + r;
        s = new byte[b]; // s = 0^b
        Z = new byte[0];
        
        // Via de regra, c = 2*hashBits 
        // Só com os parâmetros default isso não ocorre.
        // by @pbarreto
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
        s = transf.f(s); // s = f (s)

        if (zLength > r)
            return null; // TODO: seria melhor se fosse uma exception
        if (z == null)
            z = new byte[zLength];
        for (int i=0; i<zLength; i++) //  ⌊s⌋l;
            z[i] = s[i];
        return z;
    }

    /**
     * Minimal sponge padding (10*1)
     * 
     * @param mlength comprimento em bytes da mensagem do padding
     * @return o fim do padding que deve ser concatenado com o resto da mensagem
     */
    private byte[] pad101(int mLength) {
        
        // TODO: rever
        int q = r-mLength;
//        int q = (-mLength - 10) % this.r; 
        byte[] pad = new byte[q];
        pad[0] = (byte) 0x80;
        pad[q-1] = (byte) (0x01 | pad[q-1]);
        
        return pad;
    }
}
