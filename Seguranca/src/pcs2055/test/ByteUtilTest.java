package pcs2055.test;

import static org.junit.Assert.*;

import org.junit.Test;

import pcs2055.math.ByteUtil;

public class ByteUtilTest {

    @Test
    public void testConvertHexString() {

        // 1
        byte[] expected = new byte[] {0x1A, 0x0C, 0x34};
        byte[] value = ByteUtil.convertHexString("1A0C34");
        assertArrays(expected, value);

        // 2
        expected = new byte[] {0x0A, 0x0C, 0x34};
        value = ByteUtil.convertHexString("A0C34");
        assertArrays(expected, value);

        // 3
        expected = new byte[] {0x7A, 1, 0, 0, (byte) 0x80};
        value = ByteUtil.convertHexString("7A01000080");
        assertArrays(expected, value);
    }
    
    private static void assertArrays(byte[] expected, byte[] value) {
        
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], value[i]);
        }
    }
    
}
