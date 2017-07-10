package com.sample.testcase;

import junit.framework.TestCase;

import org.junit.Test;

public class SampleTestCase extends TestCase {

	@Test
	public void testAddition() {
		System.out.println("Calling testSampleMethod1");

		int a = 2, b = 5;
		int c = a + b;
		assertEquals("a+b", c, 5);
	}

	@Test
	public void testSubstraction() {
		System.out.println("Calling testSampleMethod1");

		int a = 2, b = 5;
		int c = a - b;
		assertEquals("a-b", c, -3);
	}
	
	@Test
	public void testMultiplication() {
		System.out.println("Calling testSampleMethod1");

		int a = 2, b = 5;
		int c = a * b;
		assertEquals("a*b", c, 10);
	}
	
	@Test
	public void testDivistion() {
		System.out.println("Calling testSampleMethod1");

		int a = 20, b = 5;
		int c = a / b;
		assertEquals("a/b", c, 4);
	}
	
}
