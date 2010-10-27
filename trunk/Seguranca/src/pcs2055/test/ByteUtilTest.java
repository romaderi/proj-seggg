package pcs2055.test;

import static org.junit.Assert.*;

import org.junit.Test;

import pcs2055.math.ByteUtil;

public class ByteUtilTest {

    @Test
    public void testConvertHexString() {

        // 1
        int[] expected = new int[] {0x1A, 0x0C, 0x34};
        int[] value = ByteUtil.convertHexString("1A0C34");
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], value[i]);
        }

        // 2
        expected = new int[] {0x0A, 0x0C, 0x34};
        value = ByteUtil.convertHexString("A0C34");
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], value[i]);
        }

        // 3
        expected = new int[] {0x7A, 1, 0, 0, 0x80};
        value = ByteUtil.convertHexString("7A01000080");
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], value[i]);
        }

    }

}
