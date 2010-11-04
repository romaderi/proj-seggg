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
        assertArrayEquals(expected, value);

        // 2
        expected = new byte[] {0x0A, 0x0C, 0x34};
        value = ByteUtil.convertHexString("A0C34");
        assertArrayEquals(expected, value);

        // 3
        expected = new byte[] {0x7A, 1, 0, 0, (byte) 0x80};
        value = ByteUtil.convertHexString("7A01000080");
        assertArrayEquals(expected, value);
    }
    
    @Test
    public void testInvertByte() {
        
        assertEquals((byte) 0x80, ByteUtil.invertByte((byte) 0x01));
        assertEquals((byte) 0x81, ByteUtil.invertByte((byte) 0x81));
        assertEquals((byte) 1, ByteUtil.invertByte((byte) 0x80));
        assertEquals((byte) 0x7A, ByteUtil.invertByte((byte) 0x5E));
    }
}
