package org.chen.codegen.exception;

public class CodegenException extends Exception {
	private static final long serialVersionUID = 1226430826318382469L;

	public CodegenException(String paramString) {
		super(paramString);
	}

	public CodegenException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public CodegenException(Throwable paramThrowable) {
		super(paramThrowable);
	}
}