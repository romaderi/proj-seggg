package pcs2055.test.keccak;

import pcs2055.math.ByteUtil;

/**
 * Representa os resultados de um round do keccak
 *
 */
public class Round {

    private byte[] teta;
    private byte[] rho;
    private byte[] pi;
    private byte[] chi;
    private byte[] iota;
    
    public byte[] beforeTeta() {
        return teta;
    }
    public void setTeta(byte[] teta) {
        this.teta = teta;
    }
    public byte[] beforeRho() {
        return rho;
    }
    public void setRho(byte[] rho) {
        this.rho = rho;
    }
    public byte[] beforePi() {
        return pi;
    }
    public void setPi(byte[] pi) {
        this.pi = pi;
    }
    public byte[] beforeChi() {
        return chi;
    }
    public void setChi(byte[] chi) {
        this.chi = chi;
    }
    public byte[] beforeIota() {
        return iota;
    }
    public void setIota(byte[] iota) {
        this.iota = iota;
    }

    public void print() {
        ByteUtil.printArray(teta, "theta= ");
        ByteUtil.printArray(rho, "rho= ");
        ByteUtil.printArray(pi, "pi= ");
        ByteUtil.printArray(chi, "chi= ");
        ByteUtil.printArray(iota, "iota= ");    
    }
    
    
}
