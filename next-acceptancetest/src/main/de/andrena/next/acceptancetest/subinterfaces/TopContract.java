package de.andrena.next.acceptancetest.subinterfaces;

import static de.andrena.next.Condition.ignored;
import static de.andrena.next.Condition.result;
import de.andrena.next.ClassInvariant;
import de.andrena.next.Condition;

public class TopContract implements Top {

	@Override
	public int pre(String parameter) {
		if (Condition.pre()) {
			assert parameter != null : "parameter must not be null";
		}
		return ignored();
	}

	@Override
	public int post(String parameter) {
		if (Condition.post()) {
			assert result(Integer.class) >= 0 : "result >= 0";
		}
		return ignored();
	}

	@Override
	public int preAndPost(String parameter) {
		if (Condition.pre()) {
			assert parameter != null : "parameter must not be null";
		}
		if (Condition.post()) {
			assert result(Integer.class) >= 0 : "result >= 0";
		}
		return ignored();
	}

	@Override
	public int invariant(String parameter) {
		return ignored();
	}

	@ClassInvariant
	public void aIsAlwaysAMultipleOfTwo() {
		assert target().pre("") % 2 == 0 : "a() is a multiple of two";
	}

	@Override
	public int unchanged() {
		if (Condition.post()) {
			assert Condition.unchanged(target().unchanged()) : "unchanged never changes";
		}
		return ignored();
	}

	private Top target() {
		return Condition.target();
	}

}
