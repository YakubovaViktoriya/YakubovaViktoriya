package main;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

import main.operators.IOperator;
import main.throwsCalculator.CalculatorException;
import main.throwsCalculator.CalculatorException.ErrorCode;
/**
 * Expands complex calculator with new functions
 * @author YakubovaVictoryia
 */
public class FunctionalCalculator extends ComplexCalculator {
	
	/** list of all described unary operators */
	protected static final List<Unary_Operator> UNARY_OPERATORS=
			 Arrays.asList(Unary_Operator.values());
	
	/** Description of unary operators*/
	protected enum Unary_Operator implements DoubleUnaryOperator, IOperator{
		SIN ("sin",Math :: sin),
		COS ("cos", Math :: cos),
		TG 	("tg", Math :: tan),
		SQRT ("√", (op) -> {
			if (op<0)
				try {
					throw new CalculatorException(ErrorCode.NEGATIVE_NUMBER);
				} catch (CalculatorException e) {
					errExpression=e.toString();
				}
			return Math.sqrt(op);
		});

		private String name;
		private DoubleUnaryOperator  doubleUnaryOperator;
		
		
		private Unary_Operator (String name, DoubleUnaryOperator doubleUnaryOperator){
			this.name=name;
			this.doubleUnaryOperator=doubleUnaryOperator;
		}
		
		public String getName(){
			return name;
		}
		
		public int getPriority(){
			return Integer.MAX_VALUE;
		}

		
		@Override
		public double applyAsDouble(double arg0) {
			return doubleUnaryOperator.applyAsDouble(arg0);
		}		
		
	}	
	
	/**
	 * @param operatorName
	 * @return Unary_Operator by operatorName
	 * @throws CalculatorException
	 */
	public static Unary_Operator getUnaryFunction(String operatorName) 
			throws CalculatorException{
		return UNARY_OPERATORS.stream().filter(op -> op.getName().equals(operatorName)).
				findFirst().
				orElseThrow(() -> new CalculatorException(ErrorCode.SYNTAX_ERR));
	}
	
	/**
	 * @param operatorName
	 * @return true if the unary operator with this name exists, else false
	 */
	public static boolean isUnaryFunction(String operatorName){
		return UNARY_OPERATORS.stream().anyMatch(op -> 
					op.getName().equals(operatorName));
	}

	@Override
	protected int getPriorityOperator(String operator) throws CalculatorException{
		if (isBinaryOperator(operator))
			return getBinaryOperator(operator).getPriority();
		else
			return getUnaryFunction(operator).getPriority();
	}
	
	@Override
	protected Set<String> getAllOperationsName(){
		Set<String> operationsStr= new HashSet<String>(
				BINARY_OPERATORS.stream().map(
						(op) -> op.getName()).collect(Collectors.toSet()));
			
		operationsStr.addAll(UNARY_OPERATORS.stream().map(
				(func) -> func.getName()).collect(Collectors.toSet()));
		return operationsStr;
	}
	
	@Override
	protected void calculation(Deque<Double> stack, String token) throws CalculatorException{
		if (stack.isEmpty())
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);

		if (isBinaryOperator(token))
			super.calculation(stack, token);
		else
		{
			Double operand=stack.pop();
			stack.push(getUnaryFunction(token).applyAsDouble(operand));
		}	
	}

}
