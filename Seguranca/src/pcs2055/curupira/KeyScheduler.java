package pcs2055.curupira;

public class KeyScheduler {
    
    public enum Mode {ENCRYPTING, DECRYPTING};
    
    private byte[] key;
    private int t; // 2, 3 ou 4 de acordo com tamanho da chave
    private int round;
    private Mode mode;

    /**
     * 
     * @param key
     * @param t tamanho da chave
     * @param mode criptografia ou descriptografia
     * @param roundMax quantidade de rounds
     */
    public KeyScheduler(byte[] key, int t, Mode mode, int roundMax) {
        
        this.key = key;
        this.t = t / 48;
        
        if (mode == Mode.ENCRYPTING) {
            this.round = -1;
        }
        else {
            this.round = roundMax;
        }
        this.mode = mode;
    }
    
    public byte[] nextSubKey() {
        
        if (this.mode == Mode.ENCRYPTING)
            this.round++;
        else
            this.round--;
        
        byte[] o = omega(this.key);
        for (int i=2; i<=round; i++) {
            
            o = omega(o); 
        }
        
        byte keyStage = o + accumulatedScheduleConstant(this.round); // soma de matrizes
        
        return fi(keyStage);
    }
    
    
    private byte[] omega(byte[] b) {
        
        return mi(csi(b));
    }
    
    /**
     * 
     * @param s round
     * @param t tamanho da chave
     * @return
     */
    private byte[] scheduleConstant(int s, int t) {
        
        return null;
    }
    
    /**
     * 
     * @param s round
     * @return
     */
    private byte[] accumulatedScheduleConstant(int s) {
        
        byte[] result = new byte[];
        for (int i=0; i<=s; i++) {
            
            byte[] q = scheduleConstant(s, this.t);
            byte o = new byte[];
            for (int j=1; j<=s-i+1; j++) // s-i+1 ou s-i ???
                o += omega(q); // soma de matrizes
        }
        
        
        return result;
    }
    
    private byte[] csi(byte[] b) {
        
        return null;
    }

    private byte[] mi(byte[] b) {
        
        return null;
    }
    
    /**
     * key selection
     * @param K key stage
     * @return
     */
    private byte[] fi(byte[] K) {
        
        // cópia de K trocando a primeira linha com o resultado da primeira linha de sbox(K)
        // limita aqui a sub-chave a 96 bits

        return null;
    }
    

}
