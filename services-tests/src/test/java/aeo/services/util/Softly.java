package aeo.services.util;

import java.util.regex.Matcher;

import org.junit.Assert;

public class Softly {

	private static StringBuilder strbldr = new StringBuilder();
	private static String lineSep = System.getProperty("line.separator");
	private static int counter = 0;
	public static int runCounter = 0;
	public static int failCounter = 0;

	private static void resetParams() {
		strbldr = new StringBuilder();
		counter = 0;
	}

	

	public static void assertAll() {
		String assertFailLst = strbldr.toString();
		int failureCount = counter;
		resetParams();
		if (assertFailLst.length() != 0) {
			failCounter++;
			
		}
		runCounter++;
	}

	public static void assertEquals(String msg, Object expected, Object actual) {
		try {
			Assert.assertEquals(msg, expected, actual);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertTrue(String msg, boolean condition) {
		try {
			Assert.assertTrue(msg, condition);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertFalse(String msg, boolean condition) {
		try {
			Assert.assertFalse(msg, condition);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertNull(String msg, Object object) {
		try {
			Assert.assertNull(msg, object);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertNotNull(String msg, Object object) {
		try {
			Assert.assertNotNull(msg, object);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertSame(String msg, Object expected, Object actual) {
		try {
			Assert.assertSame(msg, expected, actual);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertNotSame(String msg, Object expected, Object actual) {
		try {
			Assert.assertNotSame(msg, expected, actual);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	public static void assertArrayEquals(String msg, Object[] expected, Object[] actual) {
		try {
			Assert.assertArrayEquals(msg, expected, actual);
		} catch (AssertionError e) {
			counter++;
			strbldr.append(e.toString() + lineSep);
		}
	}

	

}
