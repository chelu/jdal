package util;

import java.io.IOException;

import junit.framework.TestCase;

import org.jdal.util.Serializer;

/**
 * Simple test for Serializer
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
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
