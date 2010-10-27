package pcs2055.test.keccak;

import java.util.List;

/**
 * Representa UM teste do Keccak, com os valores de entrada 
 * e os valores esperados de saída
 * 
 */
public class CaseTest {

    private int[] sigma0;
    private int[] xor1;
    private List<Round> rounds1;
    private int[] permutation1;
    private int[] z0;
    private int[] sigma1;
    private int[] xor2;
    private List<Round> rounds2;
    private int[] permutation2;
    private int[] z1;
    
    
    public int[] getSigma0() {
        return sigma0;
    }
    public int[] afterXor1() {
        return xor1;
    }
    public List<Round> getRounds1() {
        return rounds1;
    }
    public int[] afterPermutation1() {
        return permutation1;
    }
    public int[] getZ0() {
        return z0;
    }
    public int[] getSigma1() {
        return sigma1;
    }
    public int[] afterXor2() {
        return xor2;
    }
    public List<Round> getRounds2() {
        return rounds2;
    }
    public int[] afterPermutation2() {
        return permutation2;
    }
    public int[] getZ1() {
        return z1;
    }
    
    
    public void setSigma0(int[] sigma0) {
        this.sigma0 = sigma0;
    }
    public void setXor1(int[] xor1) {
        this.xor1 = xor1;
    }
    public void setRounds1(List<Round> rounds1) {
        this.rounds1 = rounds1;
    }
    public void setPermutation1(int[] permutation1) {
        this.permutation1 = permutation1;
    }
    public void setZ0(int[] z0) {
        this.z0 = z0;
    }
    public void setSigma1(int[] sigma1) {
        this.sigma1 = sigma1;
    }
    public void setXor2(int[] xor2) {
        this.xor2 = xor2;
    }
    public void setRounds2(List<Round> rounds2) {
        this.rounds2 = rounds2;
    }
    public void setPermutation2(int[] permutation2) {
        this.permutation2 = permutation2;
    }
    public void setZ1(int[] z1) {
        this.z1 = z1;
    }
    
}
