package pcs2055.test;

import pcs2055.curupira.Curupira;
import pcs2055.interfaces.AED;
import pcs2055.interfaces.BlockCipher;
import pcs2055.interfaces.MAC;
import pcs2055.ls.LetterSoup;
import pcs2055.marvin.Marvin;
import pcs2055.math.ByteUtil;

public class LSTest {

    public static void main(String[] args) {

        byte[] plainText = new byte[23];
        byte[] key = new byte[12];
        byte[] nounce = new byte[12];
        nounce[11] = 0x2A;
        
        BlockCipher curupira = new Curupira();
        MAC marvin = new Marvin();
        AED ls = new LetterSoup();
        
        ls.setKey(key, Curupira.KEY_SIZE_96);
        ls.setCipher(curupira);
        ls.setIV(nounce, 12);
        ls.setMAC(marvin);

        byte[] cData = new byte[12]; // inútil aqui
        byte[] crypto = ls.encrypt(plainText, 23, cData);
        
        System.out.println("Ciphertext=");
        ByteUtil.printArray(crypto);
        
        byte[] tag = ls.getTag(cData, 12*8);

        System.out.println("Tag=");
        ByteUtil.printArray(tag);

    }

}
