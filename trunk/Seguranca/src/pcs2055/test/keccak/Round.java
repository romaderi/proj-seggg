package pcs2055.test.keccak;

/**
 * Representa os resultados de um round do keccak
 *
 */
public class Round {

    private int[] teta;
    private int[] rho;
    private int[] pi;
    private int[] chi;
    private int[] iota;
    
    public int[] beforeTeta() {
        return teta;
    }
    public void setTeta(int[] teta) {
        this.teta = teta;
    }
    public int[] beforeRho() {
        return rho;
    }
    public void setRho(int[] rho) {
        this.rho = rho;
    }
    public int[] beforePi() {
        return pi;
    }
    public void setPi(int[] pi) {
        this.pi = pi;
    }
    public int[] beforeChi() {
        return chi;
    }
    public void setChi(int[] chi) {
        this.chi = chi;
    }
    public int[] beforeIota() {
        return iota;
    }
    public void setIota(int[] iota) {
        this.iota = iota;
    }
    
    
}
