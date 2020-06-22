package edu.cg;

@SuppressWarnings("serial")
public class UnimplementedMethodException extends RuntimeException {
	
	public UnimplementedMethodException(String methodName) {
		super("Method " + methodName + " is not implemented.");
	}
}
