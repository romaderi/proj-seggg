package pcs2055.sponge;

import java.util.Arrays;

import pcs2055.math.ByteUtil;

public class SpongePRNG implements SpongeRandom {
    
    // c = b - r
    private int r; // bitrate
    private int c; // capacity
    private Duplex D; // esponja
    private byte[] Bin, Bout;

    @Override
    public void init(int hashBits) {
        
        D = new SpongeDuplex();
        D.init(hashBits);
        Bin = new byte[0];
        Bout = new byte[0];
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
    public void feed(byte[] sigma, int sigmaLength) {

        // P.feed(σ) with σ ∈ Z²∗

        byte[] M = ByteUtil.append(Bin, sigma, Bin.length, sigmaLength);
        int rho = 8; // ρmax (pad, r) = max{x : x + |pad[r](x)| ≤ r}.
        
        // vamos supor rho múltipl de 8!
        int w = 8*M.length / rho;
        byte[] z = new byte[M.length];
        byte[] Mi = null;
        for (int i=0; i<w-1; i+=rho/8) {
            Mi = Arrays.copyOfRange(M, i, i+rho/8);
            D.duplexing(Mi, rho, z, M.length);
        }
        Bin = Mi;
        Bout = new byte[0];
    }

    @Override
    public byte[] fetch(byte[] z, int zLength) {
        return null;
    }

    @Override
    public void forget() {
        
    }


}
