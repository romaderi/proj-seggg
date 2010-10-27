package pcs2055.test.keccak;

import java.util.List;

/**
 * Representa UM teste do Keccak, com os valores de entrada 
 * e os valores esperados de saída
 * 
 */
public class CaseTest {

    private byte[] sigma0;
    private byte[] xor1;
    private List<Round> rounds1;
    private byte[] permutation1;
    private byte[] z0;
    private byte[] sigma1;
    private byte[] xor2;
    private List<Round> rounds2;
    private byte[] permutation2;
    private byte[] z1;
    
    
    public byte[] getSigma0() {
        return sigma0;
    }
    public byte[] afterXor1() {
        return xor1;
    }
    public List<Round> getRounds1() {
        return rounds1;
    }
    public byte[] afterPermutation1() {
        return permutation1;
    }
    public byte[] getZ0() {
        return z0;
    }
    public byte[] getSigma1() {
        return sigma1;
    }
    public byte[] afterXor2() {
        return xor2;
    }
    public List<Round> getRounds2() {
        return rounds2;
    }
    public byte[] afterPermutation2() {
        return permutation2;
    }
    public byte[] getZ1() {
        return z1;
    }
    
    
    public void setSigma0(byte[] sigma0) {
        this.sigma0 = sigma0;
    }
    public void setXor1(byte[] xor1) {
        this.xor1 = xor1;
    }
    public void setRounds1(List<Round> rounds1) {
        this.rounds1 = rounds1;
    }
    public void setPermutation1(byte[] permutation1) {
        this.permutation1 = permutation1;
    }
    public void setZ0(byte[] z0) {
        this.z0 = z0;
    }
    public void setSigma1(byte[] sigma1) {
        this.sigma1 = sigma1;
    }
    public void setXor2(byte[] xor2) {
        this.xor2 = xor2;
    }
    public void setRounds2(List<Round> rounds2) {
        this.rounds2 = rounds2;
    }
    public void setPermutation2(byte[] permutation2) {
        this.permutation2 = permutation2;
    }
    public void setZ1(byte[] z1) {
        this.z1 = z1;
    }
    

    
    
    
}
