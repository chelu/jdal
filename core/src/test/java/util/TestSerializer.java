package util;

import info.joseluismartin.util.Serializer;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Simple test for Serializer
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class TestSerializer extends TestCase {
	
	/** 
	 * Test serializer
	 * @throws IOException ioe
	 */
	public void testSerializer() throws IOException {
		Integer i = 2008;
		byte[] ser = Serializer.serialize(i);
		Integer j = (Integer) Serializer.deSerialize(ser);
		assertEquals(i, j);
	}
}
