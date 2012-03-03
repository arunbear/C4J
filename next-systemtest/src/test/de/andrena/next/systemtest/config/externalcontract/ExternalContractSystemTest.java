package de.andrena.next.systemtest.config.externalcontract;

import static de.andrena.next.Condition.pre;

import org.junit.Rule;
import org.junit.Test;

import de.andrena.next.Contract;
import de.andrena.next.systemtest.TransformerAwareRule;

public class ExternalContractSystemTest {
	@Rule
	public TransformerAwareRule transformerAwareRule = new TransformerAwareRule();

	@Test(expected = AssertionError.class)
	public void testExternalContract() {
		new TargetClass().method(0);
	}

	public static class TargetClass {
		public void method(int arg) {
		}
	}

	public static class ContractClass extends TargetClass {
		@Override
		public void method(int arg) {
			if (pre()) {
				assert arg > 0;
			}
		}
	}

	@Test
	public void testLocalContractPreferred() {
		new TargetClassWithLocalAndExternalContract().method(1);
	}

	@Test(expected = AssertionError.class)
	public void testLocalContractPreferredFailing() {
		new TargetClassWithLocalAndExternalContract().method(0);
	}

	@Contract(LocalContract.class)
	public static class TargetClassWithLocalAndExternalContract {
		public void method(int arg) {
		}
	}

	public static class LocalContract extends TargetClassWithLocalAndExternalContract {
		@Override
		public void method(int arg) {
			if (pre()) {
				assert arg > 0;
			}
		}
	}

	public static class ExternalContract extends TargetClassWithLocalAndExternalContract {
		@Override
		public void method(int arg) {
			if (pre()) {
				assert arg > 1;
			}
		}
	}
}