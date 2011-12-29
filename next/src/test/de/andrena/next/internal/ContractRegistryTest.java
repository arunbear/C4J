package de.andrena.next.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javassist.ClassPool;
import javassist.CtClass;

import org.junit.Before;
import org.junit.Test;

import de.andrena.next.internal.ContractRegistry.ContractInfo;

public class ContractRegistryTest {

	private ContractRegistry contractRegistry;
	private ClassPool pool;
	private CtClass targetClass;
	private CtClass contractClass;
	private CtClass innerContractClass;

	@Before
	public void before() throws Exception {
		contractRegistry = new ContractRegistry();
		pool = ClassPool.getDefault();
		targetClass = pool.get(TargetClass.class.getName());
		contractClass = pool.get(ContractClass.class.getName());
		innerContractClass = pool.get(InnerContractClass.class.getName());
	}

	@Test
	public void testRegisterContract() {
		ContractInfo contractInfo = contractRegistry.registerContract(targetClass, contractClass);
		assertEquals(targetClass, contractInfo.getTargetClass());
		assertEquals(contractClass, contractInfo.getContractClass());
		assertTrue(contractInfo.getInnerContractClasses().isEmpty());
		assertEquals(1, contractInfo.getAllContractClasses().size());
		assertTrue(contractInfo.getAllContractClasses().contains(contractClass));
	}

	@Test
	public void testIsContractClass() {
		assertFalse(contractRegistry.isContractClass(contractClass));
		contractRegistry.registerContract(targetClass, contractClass);
		assertTrue(contractRegistry.isContractClass(contractClass));
	}

	@Test
	public void testAddInnerContractClass() {
		ContractInfo contractInfo = contractRegistry.registerContract(targetClass, contractClass);
		contractInfo.addInnerContractClass(innerContractClass);
		assertTrue(contractRegistry.isContractClass(innerContractClass));
		assertTrue(contractInfo.getInnerContractClasses().contains(innerContractClass));
		assertTrue(contractInfo.getAllContractClasses().contains(innerContractClass));
	}

	@Test
	public void testGetAllContractClasses() {
		ContractInfo contractInfo = contractRegistry.registerContract(targetClass, contractClass);
		assertEquals(1, contractInfo.getAllContractClasses().size());
		assertTrue(contractInfo.getAllContractClasses().contains(contractClass));
		contractInfo.addInnerContractClass(innerContractClass);
		assertEquals(2, contractInfo.getAllContractClasses().size());
		assertTrue(contractInfo.getAllContractClasses().contains(innerContractClass));
	}

	public static class TargetClass {
	}

	public static class ContractClass extends TargetClass {
	}

	public static class InnerContractClass {
	}
}
