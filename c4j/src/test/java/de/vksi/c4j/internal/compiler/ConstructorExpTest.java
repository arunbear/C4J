package de.vksi.c4j.internal.compiler;

import static org.junit.Assert.assertEquals;
import javassist.ClassPool;

import org.junit.Test;

import de.vksi.c4j.internal.compiler.ConstructorExp;
import de.vksi.c4j.internal.compiler.ValueExp;

public class ConstructorExpTest {

	@Test
	public void testConstructorExpForClass() {
		assertEquals("new java.lang.Integer(5)", new ConstructorExp(Integer.class, new ValueExp(5)).getCode());
	}

	@Test
	public void testConstructorExpForCtClass() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		assertEquals("new java.lang.Integer(5)",
				new ConstructorExp(pool.get(Integer.class.getName()), new ValueExp(5)).getCode());
	}

	@Test
	public void testConstructorExpForStringClass() {
		assertEquals("new java.lang.Integer(5)", new ConstructorExp(Integer.class.getName(), new ValueExp(5)).getCode());
	}
}
