package pcs2055.curupira;

public class Round {
        
    public static byte[] initialKeyAddition(byte[] block, byte[] subkey) {
        
        return null;
    }
    
    
    public static byte[] roundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = teta(block);
        block = sigma(block, subkey);
        return block;
    }
    
    public static byte[] lastRoundFunction(byte[] block, byte[] subkey) {
        
        block = gama(block);
        block = pi(block);
        block = sigma(block, subkey);
        return block;
    }
    
    private static byte[] gama(byte[] a) {
        
        return null;
    }
    
    private static byte[] pi(byte[] a) {
        
        return null;
    }
    
    private static byte[] teta(byte[] a) {
        
        return null;
    }
    
    private static byte[] sigma(byte[] a, byte[] subkey) {
        
        return null;
    }

}
