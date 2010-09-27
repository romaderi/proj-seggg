package pcs2055.test;

import pcs2055.curupira.Round;
import pcs2055.math.ByteUtil;

public class mathTest {
    
    public static void main(String[] args) {
        
        byte[] x = {1, 2, 3, 1, 2, 3};
        byte[] y = {1, 2, 3, 1, 2, 3};
        ByteUtil.mult3xn(x, y, 2);
        
        //byte[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        //byte[] b = new byte[12];
        //b = Round.gama(a); --> aparentemente ok
        //ByteUtil.printArray(b);

        //byte[] a = {0, 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
        //byte[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        //byte[] c = Round.sigma(a, b);  --> aparentemente ok
        //ByteUtil.printArray(c);
    }

}
