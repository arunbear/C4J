package de.vksi.c4j.acceptancetest.floatingwindow;

import static de.vksi.c4j.Condition.ignored;
import static de.vksi.c4j.Condition.preCondition;

public final class SouthWestFloatingWindowSpecContract implements SouthWestFloatingWindowSpec {

	@Override
	public Vector getUpperLeftCorner() {
		// No contracts identified yet
		return ignored();
	}

	@Override
	public int getWidth() {
		// No contracts identified yet
		return (Integer) ignored();
	}

	@Override
	public int getHeight() {
		// No contracts identified yet
		return (Integer) ignored();
	}

	@Override
	public void move(Vector vector) {
		if (preCondition()) {
			assert vector.getX() < 0 : "vector.x < 0";
			assert vector.getY() < 0 : "vector.y < 0";
		}
	}

}
