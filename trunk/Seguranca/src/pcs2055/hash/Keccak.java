package pcs2055.hash;

public class Keccak implements HashFunction {

	private int b = 1600;
	private int l = 6; // b = 25*(2^l)
	private int r = 1024; // default value
	private int c = b - r; // default value = 526
	private int d = 0; // default value

    @Override
    public void init(int hashBits) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(byte[] aData, int aLength) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public byte[] getHash(byte[] val) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void pad(int M, int n){
    	
    }
    
    private void enc(int x, int n){
    	
    }

}
