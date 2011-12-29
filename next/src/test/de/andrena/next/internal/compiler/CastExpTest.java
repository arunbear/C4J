package de.andrena.next.internal.compiler;

import static org.junit.Assert.assertEquals;
import javassist.ClassPool;
import javassist.CtClass;

import org.junit.Test;

public class CastExpTest {

	@Test
	public void testCastExpForClass() {
		assertEquals("((java.lang.String) \"stringValue\")",
				new CastExp(String.class, new ValueExp("stringValue")).getCode());
	}

	@Test
	public void testCastExpForCtClass() throws Exception {
		CtClass stringClass = ClassPool.getDefault().get(String.class.getName());
		assertEquals("((java.lang.String) \"stringValue\")",
				new CastExp(stringClass, new ValueExp("stringValue")).getCode());
	}

	@Test
	public void testForReturnType() {
		assertEquals("(($r) \"stringValue\")", CastExp.forReturnType(new ValueExp("stringValue")).getCode());
	}
}
