package pcs2055.sponge;

import java.util.Arrays;

import pcs2055.hash.Keccak;
import pcs2055.math.ByteUtil;

public class SpongePRNG implements SpongeRandom {
    
    private int rho;
    private Duplex D; // esponja
    private byte[] Bin, Bout;

    @Override
    public void init(int hashBits) {
        
        D = new Keccak();
        D.init(hashBits);
        rho = D.getBitRate();
        Bin = new byte[0];
        Bout = new byte[0];
    }

    @Override
    public int getBitRate() {
        return D.getBitRate();
    }

    @Override
    public int getCapacity() {
        return D.getCapacity();
    }

    @Override
    public void feed(byte[] sigma, int sigmaLength) {

        // P.feed(σ) with σ ∈ Z²∗

        byte[] M = ByteUtil.append(Bin, sigma, Bin.length, sigmaLength); // M = Bin ||σ

        int w = M.length / rho;
        byte[] z = new byte[0];
        for (int i=0; i<w-1; i++) { // varre todos Mi, exceto o último Mi 
            byte[] Mi = Arrays.copyOfRange(M, i*rho, i*rho+rho);
            D.duplexing(Mi, Mi.length, z, 0); // D.duplexing(Mi , 0)
        }
        Bin = Arrays.copyOfRange(M, M.length-1, M.length); // Bin = Mw
        Bout = new byte[0]; 
    }

    @Override
    public byte[] fetch(byte[] z, int zLength) {
        
        // Z = P.fetch(l) with integer l ≥ 0 and Z ∈ Zl²
        
        while (Bout.length < zLength) { // while | Bout | < l
            
            byte[] bob = D.duplexing(Bin, Bin.length, z, rho);
            Bout = ByteUtil.append(Bout, bob, Bout.length, rho); // Bout = Bout ||D.duplexing(Bin , ρ)
            Bin = new byte[0]; 
        }
        
        z = Arrays.copyOfRange(Bout, 0, zLength); // Z = ⌊Bout ⌋l
        Bout = Arrays.copyOfRange(Bout, zLength, Bout.length); // Bout = last (|Bout | − l) bits of Bout
        
        return z;
    }

    @Override
    public void forget() {
    
        // Z = P.forget()

        byte[] z = D.duplexing(Bin, Bin.length, null, rho); // Z  = D.duplexing(Bin , ρ)
        Bin = new byte[0]; 
        int cr = D.getCapacity() / D.getBitRate();
        for (int i=1; i<=cr; i++) { // for i = 1 to ⌊c/r⌋ do
            z = D.duplexing(z, z.length, z, rho); // Z = D.duplexing(Z, ρ)
        }
        Bout = new byte[0]; 
    }


}
