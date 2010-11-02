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

        byte[] M = ByteUtil.append(Bin, sigma, Bin.length, sigmaLength); // M = Bin ||σ
        int rho = 8; // ρmax (pad, r) = max{x : x + |pad[r](x)| ≤ r}.
        
        // vamos supor rho múltipl de 8!
        int w = 8*M.length / rho;
        byte[] z = new byte[M.length];
        byte[] Mi = null;
        for (int i=0; i<w-1; i+=rho/8) {
            Mi = Arrays.copyOfRange(M, i, i+rho/8);
            D.duplexing(Mi, rho, z, M.length); // D.duplexing(Mi , 0)
        }
        Bin = Mi; // Bin = Mw
        Bout = new byte[0]; 
    }

    @Override
    public byte[] fetch(byte[] z, int zLength) {
        
        // Z = P.fetch(l) with integer l ≥ 0 and Z ∈ Zl²
        
        while (Bout.length < zLength) { // while | Bout | < l
            
            byte[] bob = D.duplexing(Bin, Bin.length, z, zLength);
            Bout = ByteUtil.append(Bout, bob, Bout.length, zLength); // Bout = Bout ||D.duplexing(Bin , ρ)
            Bin = new byte[0]; 
        }
        
        z = Arrays.copyOfRange(Bout, 0, zLength); // Z = ⌊Bout ⌋l
        Bout = Arrays.copyOfRange(Bout, zLength, Bout.length); // Bout = last (|Bout | − l) bits of Bout
        
        return null;
    }

    @Override
    public void forget() {
    
        // Z = P.forget()

        int rho = 8; // ρmax (pad, r) = max{x : x + |pad[r](x)| ≤ r}
        
        byte[] z = D.duplexing(Bin, rho, null, 0); // Z  = D.duplexing(Bin , ρ)
        Bin = new byte[0]; 
        for (int i=1; i<=c/r; i++) { // for i = 1 to ⌊c/r⌋ do
            z = D.duplexing(z, rho, null, 0); // Z = D.duplexing(Z, ρ)
        }
        Bout = new byte[0]; 
    }


}
