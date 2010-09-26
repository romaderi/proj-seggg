package pcs2055.curupira;

public class Round {
        
    // ???
    public byte[] firstRoundFunction(byte[] block, byte[] subkey) {
        
        return null;
    }
    
    
    public byte[] roundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = teta(block);
        block = sigma(block, subkey);
        return block;
    }
    
    public byte[] lastRoundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = sigma(block, subkey);
        return block;
    }
    
    private byte[] gama(byte[] a) {
        
        return null;
    }
    
    private byte[] pi(byte[] a) {
        
        return null;
    }
    
    private byte[] teta(byte[] a) {
        
        return null;
    }
    
    private byte[] sigma(byte[] a, byte[] subkey) {
        
        return null;
    }

}
