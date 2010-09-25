package pcs2055.curupira;

public class KeyScheduler {
    
    private byte[] key;
    private byte[] currentSubKey;
    
    public KeyScheduler(byte[] key) {
        
        this.key = key;
    }
    
    public byte[] nextSubKey() {
        
        return null;
    }

}
