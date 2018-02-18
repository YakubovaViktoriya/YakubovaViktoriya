package main;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import main.throwsCalculator.CalculatorException;
import main.throwsCalculator.CalculatorException.ErrorCode;

/**
 * The calculator expands the capabilities of a SimpleCaculator and 
 * can compute complex expressions.
 * <br>Example: (3^2+3)^5-2
 */
public class ComplexCalculator extends SimpleCalculator{
	
	@Override
	public void parser(String expression) throws CalculatorException{
		setResult(calculateRPN(sortingStation(expression,"(",")")));
	}
	
	/**
	 * @param operator
	 * @return operation priority
	 * @throws CalculatorException
	 */
	protected int getPriorityOperator(String operator) throws CalculatorException{
		return getBinaryOperator(operator).getPriority();
	}

	/**
	 * @return all operation names
	 */
	protected Set<String> getAllOperationsName(){
		return BINARY_OPERATORS.stream().map(
				(op) -> op.getName()).collect(Collectors.toSet());
	}
	
	/**
	 * @param name
	 * @return true, if operator with this name exists, else false
	 */
	protected boolean isOperator(String operatorName){
		return getAllOperationsName().stream().
					anyMatch((str) -> str.equals(operatorName));
	}
	
	
	/**
	 * @param expression
	 * @param openBracket
	 * @param closeBracket
	 * @return expression in reverse Polish notation
	 * @throws CalculatorException
	 */
	protected String sortingStation(String expression,
			String openBracket, String closeBracket) throws CalculatorException{

		Queue<String> out = new LinkedList<String>();
		Deque<String> stack= new LinkedList<String>();
		expression=expression.replace(" ",""); 
		Set<String> operationsStr= new HashSet<String>(this.getAllOperationsName());
				
		operationsStr.add(openBracket);
		operationsStr.add(closeBracket);
		
		boolean findNext = true;
		int index=0;
		String previos="";
	
		while (findNext){
			int nextOperationIndex= expression.length();
			String nextOperation="";
			
			for (String operation : operationsStr){
				int i=expression.indexOf(operation,index);
				if (i>=0 && i<nextOperationIndex){
					nextOperation = operation;
					nextOperationIndex=i;
				}
			}
			
			if (nextOperationIndex==expression.length())
				findNext=false;
			else{
			
				if (index!=nextOperationIndex)
					out.add(expression.substring(index, nextOperationIndex));
				
				if (nextOperation.equals(openBracket)){
					stack.push(openBracket);
				}
				else 
					
					if (nextOperation.equals(closeBracket)){
						if (stack.isEmpty())
							throw new CalculatorException(ErrorCode.SYNTAX_ERR);
						while(!stack.peek().equals(openBracket) && stack.peek()!=null){
							out.add(stack.pop());
						if (stack.isEmpty())
							throw new CalculatorException(ErrorCode.SYNTAX_ERR);
							
					}
					stack.pop();
				}
				else{
		
					if (nextOperationIndex!=0)
						previos=expression.
								substring(nextOperationIndex-1, nextOperationIndex);
					if (nextOperation.equals("-") && (
							previos.equals(openBracket) || nextOperationIndex==0 || 
							isOperator(previos)))
					{
						out.add("0");
						stack.push(nextOperation);
					}
					else
						
					{
						while (!stack.isEmpty() && !stack.peek().equals(openBracket) && !out.isEmpty()
							&& (getPriorityOperator(nextOperation)<=
								getPriorityOperator(stack.peek()))){
							out.add(stack.pop());
						}
						stack.push(nextOperation);
					}
				}
				
				index=nextOperationIndex + nextOperation.length();	
			}
		}
		
		if (index!= expression.length()){
			out.add(expression.substring(index));
		}
		while (!stack.isEmpty())
			out.add(stack.pop());
		
		StringBuffer result=new StringBuffer();
		if (!out.isEmpty())
			result.append(out.poll());
		while (!out.isEmpty())
			result.append(" ").append(out.poll());
		
		return result.toString();
			
	}
	
	
	/**
	 * @param expression
	 * @return result of calculation reverse Polish notation
	 * @throws CalculatorException
	 */
	protected double calculateRPN(String expression) throws CalculatorException{
		StringTokenizer tokenizer = new StringTokenizer(expression," ");
		Deque<Double> stack =new LinkedList<Double>();
		
		try{
			while(tokenizer.hasMoreTokens()){
				String token=tokenizer.nextToken();
				if (!isOperator(token))
					stack.push(Double.parseDouble(token));
				else
					calculation(stack,token);
		}
		} catch(NumberFormatException e){
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);
		} catch(CalculatorException e){
			throw new CalculatorException(e.toString());
		}		
		
		if (stack.size()!=1)
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);
		return stack.pop();
	}

	
	/**
	 * Defines the operation by the token and executes it
	 * @param stack of number is calculateRPN
	 * @param token
	 * @throws CalculatorException
	 */
	protected void calculation(Deque<Double> stack, String token) throws CalculatorException{
		if (stack.isEmpty())
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);

		Double operand2=stack.pop();
		if (stack.isEmpty())
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);
		Double operand1= stack.pop();
		stack.push(getBinaryOperator(token).applyAsDouble(operand1, operand2));
	}
	
}
