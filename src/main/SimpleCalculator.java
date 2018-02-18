package main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.throwsCalculator.CalculatorException;
import main.throwsCalculator.CalculatorException.ErrorCode;

/**
 * Calculator for simple expression
 */
public class SimpleCalculator extends Calculator{
	
	/**Parses the expression
	 * @param expression
	 * @throws CalculatorException
	 */
	protected void parser(String expression) throws CalculatorException{
		double operand1=0;
		double operand2=0;
		char operation;
		String operationStr="";
		
		if (!validation(expression))
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);
		
		Pattern p= Pattern.compile("[-]?[0-9]+(\\.[0-9]+)?");
		Matcher m = p.matcher(expression);
		
		m.find();
		operand1 = Double.valueOf(m.group());
		operation=expression.charAt(m.end());
		operationStr=String.valueOf(operation);
		m.find(m.end()+1);
		operand2 = Double.valueOf(m.group());
		
		calculation(operationStr,operand1,operand2);
	}
	
	
	/**
	 * @param expression
	 * @return the expression is correct or not
	 */
	private boolean validation(String expression){
		Pattern p= Pattern.compile("(-?\\d+)[([+(/)(*)%^])-](-?\\d+)");
		Matcher m = p.matcher(expression);
		return m.find();

	}
	
	
	/**
	 * Computes the value
	 * @param operatorStr
	 * @param operand1
	 * @param operand2
	 * @throws CalculatorException
	 */
	private void calculation(String operatorStr,double operand1, double operand2) throws CalculatorException{
		Binary_Operator operator;
		if (isBinaryOperator(operatorStr))
			operator=getBinaryOperator(operatorStr);
		else 
			throw new CalculatorException(ErrorCode.SYNTAX_ERR);
					
		this.setResult(operator.applyAsDouble(operand1, operand2));

	}
	
	@Override
	public double calculate(String expression) 
			throws CalculatorException{
			
		this.setExpression(expression);
		errExpression="";		

		expression.replace(" ","");
		parser(expression);
		return this.getResult();
	}
	
	
	/**
	 * @param number
	 * @return number formatted for output
	 */
	public  static String doubleFormat(double d){
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		
		return (d%1)!=0 ? 
				new DecimalFormat("0.00000",dfs).format(d) :
					new DecimalFormat("##").format(d);	
		
	}
	
	@Override
	public String toString(){
		return (errExpression=="") ?
				this.getExpression()+" = "+doubleFormat(this.getResult()) :
				errExpression;	
	}
	
}
