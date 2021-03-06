package de.vksi.c4j.internal.compiler;

import static de.vksi.c4j.internal.classfile.ClassAnalyzer.isInitializer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.expr.Expr;

import org.apache.log4j.Logger;

public abstract class StandaloneExp extends Exp {

	public static final StandaloneExp PROCEED_AND_ASSIGN = CodeStandaloneExp.fromNested("$_ = $proceed($$)");

	private static final Logger LOGGER = Logger.getLogger(StandaloneExp.class);

	public StandaloneExp append(StandaloneExp other) {
		return CodeStandaloneExp.fromStandalone(getCode() + other.getCode(), isEmpty() && other.isEmpty());
	}

	public StandaloneExp append(NestedExp other) {
		return append(other.toStandalone());
	}

	@Override
	public abstract String getCode();

	public void insertBefore(CtBehavior behavior) throws CannotCompileException {
		if (isEmpty()) {
			return;
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("insert before " + behavior.getLongName() + ": " + this);
		}
		if (isInitializer(behavior)) {
			((CtConstructor) behavior).insertBeforeBody(getInsertCode(getCode()));
		} else {
			behavior.insertBefore(getInsertCode(getCode()));
		}
	}

	public void insertAfter(CtBehavior behavior) throws CannotCompileException {
		if (isEmpty()) {
			return;
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("insert after " + behavior.getLongName() + ": " + this);
		}
		behavior.insertAfter(getInsertCode(getCode()));
	}

	public void insertCatch(CtClass exceptionType, CtBehavior behavior) throws CannotCompileException {
		if (isEmpty()) {
			return;
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("insert catch " + behavior.getLongName() + " for " + exceptionType.getName() + ": " + this);
		}
		behavior.addCatch(getInsertCode(getCode()), exceptionType);
	}

	public void insertFinally(CtBehavior behavior) throws CannotCompileException {
		if (isEmpty()) {
			return;
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("insert finally " + behavior.getLongName() + ": " + this);
		}
		behavior.insertAfter(getInsertCode(getCode()), true);
	}

	public void replace(Expr expression) throws CannotCompileException {
		if (isEmpty()) {
			return;
		}
		expression.replace(getInsertCode(getCode()));
	}

	private String getInsertCode(String code) {
		return "{ " + code + " }";
	}

	public boolean isEmpty() {
		return false;
	}

	public static class CodeStandaloneExp extends StandaloneExp {
		private String code;
		private boolean empty;

		private CodeStandaloneExp(String code, boolean empty) {
			this.code = code;
			this.empty = empty;
		}

		protected static StandaloneExp fromStandalone(String code, boolean empty) {
			return new CodeStandaloneExp(code, empty);
		}

		protected static StandaloneExp fromNested(String code) {
			return new CodeStandaloneExp("\n" + code + ";", false);
		}

		@Override
		public boolean isEmpty() {
			return empty;
		}

		@Override
		public String getCode() {
			return code;
		}
	}
}
