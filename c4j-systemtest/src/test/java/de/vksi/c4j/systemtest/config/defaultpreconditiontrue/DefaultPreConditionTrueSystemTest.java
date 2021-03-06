package de.vksi.c4j.systemtest.config.defaultpreconditiontrue;

import static de.vksi.c4j.Condition.preCondition;

import org.apache.log4j.Level;
import org.junit.Rule;
import org.junit.Test;

import de.vksi.c4j.ContractReference;
import de.vksi.c4j.systemtest.TransformerAwareRule;

public class DefaultPreConditionTrueSystemTest {
	@Rule
	public TransformerAwareRule transformerAwareRule = new TransformerAwareRule();

	@Test
	public void testPreConditionUndefined() {
		new TargetClass().method(-1);
		transformerAwareRule.expectGlobalLog(Level.ERROR, "Found strengthening pre-condition in "
				+ ContractClass.class.getName() + ".method(int)" + " which is already defined from "
				+ SuperClass.class.getName() + " - ignoring the pre-condition.");
	}

	@ContractReference(ContractClass.class)
	private static class TargetClass extends SuperClass {
	}

	private static class ContractClass extends TargetClass {
		@Override
		public void method(int arg) {
			if (preCondition()) {
				assert arg > 0;
			}
		}
	}

	private static class SuperClass {
		public void method(int arg) {
		}
	}

}
