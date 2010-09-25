package pcs2055.curupira;

public class Round {
    
    // minibox's
    public final byte[] P = {0x3, 0xF, 0xE, 0x0, 0x5, 0x4, 0xB, 0xC, 0xD, 0xA, 0x9, 0x6, 0x7, 0x8, 0x2, 0x1}; 
    public final byte[] Q = {0x9, 0xE, 0x5, 0x6, 0xA, 0x2, 0x3, 0xC, 0xF, 0x0, 0x4, 0xF, 0x7, 0xB, 0x1, 0x8}; 
    
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
