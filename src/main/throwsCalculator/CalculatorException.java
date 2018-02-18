package main.throwsCalculator;

/**
 * Exception handler for calculators
 */
public class CalculatorException extends Exception{

	private static final long serialVersionUID = 1L;
	private String errStr;
	
	/** Main list of error messages */
	public static enum ErrorCode{
		DIV_BY_ZERO("Division by zero!"),
		SYNTAX_ERR("Syntax error!"),
		NEGATIVE_NUMBER("Square root of a negative number!");
		
		private String value="";
		
		private ErrorCode (String value){
			this.value=value;
		}
		
		public String getValue(){
			return value;
		}
		
	}

	public CalculatorException(ErrorCode err){
		super(err.getValue());
		this.errStr = err.value;
	}

	public CalculatorException(String errStr){
		super(errStr);
		this.errStr = errStr;
	}
	
	public String toString(){
		return this.errStr;
	}
}
