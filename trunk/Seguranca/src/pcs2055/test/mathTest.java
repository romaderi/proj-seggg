package pcs2055.test;

import pcs2055.math.ByteUtil;

public class mathTest {
    
    public static void main(String[] args) {
        
        byte[] x = {1, 2, 3, 1, 2, 3};
        byte[] y = {1, 2, 3, 1, 2, 3};
        ByteUtil.mult3xn(x, y, 2);

    }

}
