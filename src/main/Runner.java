package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.throwsCalculator.CalculatorException;

public class Runner {
	
	/**
	 * Reads and returns a list of expressions is file
	 * @param nameFile
	 * @return listExpressions;
	 */
	public static List<String> fileReader(String nameFile){
		List<String> listExpressions = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(
					new FileReader(new File(nameFile)))){
			String line="";
			while((line=br.readLine())!= null)
				listExpressions.add(line);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listExpressions;
	}

	/**
	 * Calculator calculates the expressions from inputFileName and
	 * writes results to outputFileName 
	 * @param inputFileName
	 * @param outputFileName
	 * @param calculator
	 */
	public static void execute(
			String inputFileName, String outputFileName, Calculator calculator){
		
		List <String> list= fileReader(inputFileName);
		
		try(BufferedWriter bw= new BufferedWriter(
				new FileWriter(new File(outputFileName)))){

			for (String str : list){
				try {
					calculator.calculate(str);
					bw.write(calculator.toString());
					bw.newLine();
										
				} catch (CalculatorException e) {
					bw.write(e.toString());
					bw.newLine();
				}
			}			
			
		} catch(IOException e){
			e.printStackTrace();
		}
			
	}
	
	public static void main(String ... args){
		
		Calculator calculator1= new SimpleCalculator ();
		execute("input_1.txt","output_1.txt",calculator1);
		
		Calculator calculator2=new ComplexCalculator();
		execute("input_2.txt","output_2.txt", calculator2);
		
		Calculator calculator3=new FunctionalCalculator();
		execute("input_3.txt", "output_3.txt", calculator3);
			
	}


}
