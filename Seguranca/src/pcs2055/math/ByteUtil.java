package pcs2055.math;

public class ByteUtil {

    public static byte[] lpad(byte[] b, int n) {
        
        return null;
    }

    public static byte[] rpad(byte[] b, int n) {
        
        return null;
    }

    public static byte[] add(byte[] a, byte[] b, int length) {
        
        byte[] c = new byte[length];
        for (int i=0; i<length; i++)
            c[i] = (byte) (a[i] + b[i]);
        
        return c;
    }

    /**
     * multiplicação de matrizes 3xn
     * @param a matriz 3xn
     * @param b matriz nx3
     * @param n 
     * @return matriz 3x3
     */
    public static byte[] mult3xn(byte[] a, byte[] b, int n) {
        
        // cria matrizes
        byte[][] ma = new byte[3][n];
        byte[][] mb = new byte[n][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<n; j++)
                ma[i][j] = (byte) a[i*n + j];
        }
        for (int i=0; i<n; i++) {
            for (int j=0; j<3; j++)
                mb[i][j] = (byte) b[i*3 + j];
        }

        // multiplica
        byte[][] mc = new byte[3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<n; k++)
                    mc[i][j] += (byte) (ma[i][k] * mb[k][j]);
            }
        }

        // volta pra forma de vetor
        byte[] c = new byte[9];
        for (int i=0; i<9; i++)
            c[i] = mc[i/3][i%3];

        return c;
    }

    public static void printArray(byte[] v) {
        
        for (int i=0; i<v.length; i++)
            System.out.print(v[i] + " ");
        System.out.println("");
    }

    public static void printMatriz(byte[][] m) {
        
        for (int i=0; i<m.length; i++) {
            for (int j=0; j<m[i].length; j++)
                System.out.print(m[i][j] + " ");
            System.out.println("");
        }
    }

    public static byte[] xor(byte[] a, byte[] b, int length) {
        
        byte[] c = new byte[length];
        for (int i=0; i<length; i++)
            c[i] = (byte) (a[i] ^ b[i]);
        
        return c;
    }
}
