package main;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import main.operators.IOperator;
import main.throwsCalculator.CalculatorException;
import main.throwsCalculator.CalculatorException.ErrorCode;

/**
 *	Used to describe the calculator
 */
public abstract class Calculator {
	/** Original expression */
	private String expression="";
	/** Error description*/
	protected static String errExpression="";
	/** Result of calculation */
	private double result;
	
	/** List of all described binary operators */
	protected static final List<Binary_Operator> BINARY_OPERATORS=
			Arrays.asList(Binary_Operator.values());
	
	/** Description of bunary operators*/
	protected enum Binary_Operator implements DoubleBinaryOperator, IOperator {
		PLUS	("+", 1,(l, r) -> l + r),
		MINUS	("-", 1, (l, r) -> l - r),
		TIMES	("*", 2, (l, r) -> l * r),
		DIVIDE	("/", 2, (l, r) -> {
			if (r==0)
				try {
					throw new CalculatorException(ErrorCode.DIV_BY_ZERO);
				} catch (CalculatorException e) {
					errExpression = e.toString();			
				}
			return l / r;}),
		POW		("^", 3, (l, r) -> Math.pow(l, r)),
		PROCENT	("%", 2, (l, r) -> r *l / 100);
		
		private String name;
		private int priority;
		private DoubleBinaryOperator binaryOperator;
		
		Binary_Operator (String name, int priority, DoubleBinaryOperator binaryOperator){
			this.name=name;
			this.priority=priority;
			this.binaryOperator=binaryOperator;
		}
		
		public int getPriority(){
			return  this.priority;
		}
		
		public String getName(){
			return this.name;
		}

		@Override
		public double applyAsDouble(double left, double right) {
			return binaryOperator.applyAsDouble(left, right);
		}	}

	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void setResult(double result){
		this.result=result;
	}
	
	public double getResult(){
		return result;
	}
	
	/**
	 * @param expression
	 * @return result of calculation
	 * @throws CalculatorException
	 */
	abstract double calculate(String expression) throws CalculatorException;
	
	/**
	 * @param operatorName
	 * @return Binary_Operator by operatorName
	 * @throws CalculatorException
	 */
	public static Binary_Operator getBinaryOperator(String operatorName) 
			throws CalculatorException{
		return BINARY_OPERATORS.stream().
				filter(op -> op.name.equals(operatorName)).
				findFirst().
				orElseThrow(() -> new CalculatorException(ErrorCode.SYNTAX_ERR));
	}
	
	/**
	 * @param operatorName
	 * @return true if the binary operator with this name exists, else false
	 * 
	 */
	public static boolean isBinaryOperator(String operatorName){
		return BINARY_OPERATORS.stream().anyMatch(op -> op.name.equals(operatorName));
	}
}
