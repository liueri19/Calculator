package org.sevenhills.liueri19;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * bug cause found: ln(0.00001) -> ln1.0E-5
 * ln(1.0) is taken first
 */

public class Calculator {
	public static final String VERSION = "1.2";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String expression;
		
		System.out.println("//Calculator Started//");
		while (true) {
			System.out.println("input expression:");
			expression = scanner.nextLine();
			
			if (expression.equalsIgnoreCase("version")) {
				System.out.printf("Version: %s%nGeneral purpose calculator. Inspired by Eddie Hatfield, implemented by Eric Liu.%n%n", VERSION);
				continue;
			}
			else if (expression.equalsIgnoreCase("exit"))
				break;
			else if (expression.equals(""))
				continue;
			
			//System.out.printf("= %f%n", evaluate(expression));
			System.out.println("= " + evaluate(expression));	//prints more precision than printf()
		}
		scanner.close();
	}
	
	public static double evaluate(String expression) {
		int subStart = 0;
		int parenthesis = 0;
		boolean isSubExpression = false;
		String subExpression = "";
		
		String tempResultString = "";
		double finalResult = 0;
		double number;
		List<Double> numbers = new ArrayList<Double>();
		List<Operation> operations = new ArrayList<Operation>();
		
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				parenthesis++;
				if (!isSubExpression) {	//if this is the start
					isSubExpression = true;
					subStart = i;
					continue;
				}
			}
			else if (expression.charAt(i) == ')') {
				parenthesis--;
				if (parenthesis == 0) {	//if this is the end
					subExpression = new BigDecimal(evaluate(subExpression)).toPlainString();
					//expression = partBefore + evaluate(subExpression) + partAfter;
					expression = expression.substring(0, subStart) 
							+ subExpression 
							+ expression.substring(i+1);
					//reset states
					i = subStart + subExpression.length();
					subStart = 0;
					isSubExpression = false;
					subExpression = "";
				}
			}
			if (isSubExpression)
				subExpression += expression.charAt(i);
		}
		
		for (int i = 0; i < expression.length(); i++) {
			char ch = expression.charAt(i);
			
			tempResultString += ch;
			//parse for constants
			if (ch == 'e') { //constant e
				tempResultString = tempResultString.substring(0, tempResultString.length()-1); //delete the 'e'
				//if there is a number before e; Exp: 2e = 2 * e
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				tempResultString += Double.toString(Math.E);
			}
			else if (ch == 'i') { //constant pi
				int lastIndex = tempResultString.length()-1; //last index of the tempResultString
				if (lastIndex >= 0) { //if there is a character before 'i'
					if (tempResultString.charAt(lastIndex-1) == 'p') { //if the character before 'i' is 'p'
						//do the calculations
						tempResultString = tempResultString.substring(0, lastIndex-1); //delete the "pi"
						if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
							number = Double.parseDouble(tempResultString);
							tempResultString = "";
							numbers.add(number);
							operations.add(Operation.MULTIPLICATION);
						}
						tempResultString += Double.toString(Math.PI);
					}
				}
			}
			////
			else if (ch == 'E') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				//aEb = a * 10^b;
				numbers.add(number);
				operations.add(Operation.MULTIPLICATION);
				numbers.add(10d);
				operations.add(Operation.EXPONENTIATION);
			}
			//parse functions
			else if (tempResultString.indexOf("log") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-3); //delete the "log"
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.LOGARITHM10);
			}
			else if (tempResultString.indexOf("ln") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-2); //delete the "ln"
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d);
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.LOGARITHMN);
			}
			else if (tempResultString.indexOf("arcsin") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-6);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.ARCSINE);
			}
			else if (tempResultString.indexOf("arccos") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-6);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.ARCCOSINE);
			}
			else if (tempResultString.indexOf("arctan") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-6);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.ARCTANGENT);
			}
			else if (tempResultString.indexOf("sin") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-3);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.SINE);
			}
			else if (tempResultString.indexOf("cos") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-3);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.COSINE);
			}
			else if (tempResultString.indexOf("tan") > -1) {
				tempResultString = tempResultString.substring(0, tempResultString.length()-3);
				if (!tempResultString.isEmpty() && !tempResultString.equals("-")) {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.MULTIPLICATION);
				}
				else if (tempResultString.equals("-")) {
					tempResultString = "";
					numbers.add(-1d); //multiply by -1
					operations.add(Operation.MULTIPLICATION);
				}
				operations.add(Operation.TANGENT);
			}
			////
			else if (ch == '+') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.ADDITION);
			}
			else if (ch == '-') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				if (tempResultString.isEmpty()) {	//encountered a negative number
					tempResultString += '-';
				}
				else {
					number = Double.parseDouble(tempResultString);
					tempResultString = "";
					numbers.add(number);
					operations.add(Operation.SUBTRACTION);
				}
			}
			else if (ch == '*') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.MULTIPLICATION);
			}
			else if (ch == '/') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.DIVISION);
			}
			else if (ch == '^') {
				tempResultString = tempResultString.substring(0, tempResultString.length()-1);
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.EXPONENTIATION);
			}
		}
		//after the for loop, add the last number
		number = Double.parseDouble(tempResultString);
		tempResultString = "";
		numbers.add(number);
		
		//process functions
		for (int i = 0; i < operations.size(); i++) {
			Operation op = operations.get(i);
			double result;
			if (op == Operation.LOGARITHM10 || op == Operation.LOGARITHMN
					|| op == Operation.SINE || op == Operation.COSINE || op == Operation.TANGENT
					|| op == Operation.ARCSINE || op == Operation.ARCCOSINE || op == Operation.ARCTANGENT) {
				result = calculate(op, numbers.get(i), 0);
				numbers.set(i, result);
				operations.remove(i);
				i--;
			}
		}
		
		//process exponentiation
 		for (int i = 0; i < operations.size(); i++) {
			Operation op = operations.get(i);
			double result;
			if (op == Operation.EXPONENTIATION) {
				result = calculate(op, numbers.get(i), numbers.get(i+1));
				numbers.set(i+1, result);
				numbers.remove(i);
				operations.remove(i);
				i--;
			}
		}
		
		//process multiplication and division
		for (int i = 0; i < operations.size(); i++) {
			Operation op = operations.get(i);
			double result;
			if (op == Operation.MULTIPLICATION || op == Operation.DIVISION) {
				result = calculate(op, numbers.get(i), numbers.get(i+1));
				numbers.set(i+1, result);
				numbers.remove(i);
				operations.remove(i);
				i--;
			}
		}
		
		//at last, process addition and subtraction
		finalResult = numbers.get(0);
		for (int i = 0; i < operations.size(); i++) {
			Operation op = operations.get(i);
			finalResult = calculate(op, finalResult, numbers.get(i+1));
		}
		
		return finalResult;
	}
	
	public static double calculate(Operation op, double a, double b) {
		if (op == Operation.ADDITION)
			return a + b;
		else if (op == Operation.SUBTRACTION)
			return a - b;
		else if (op == Operation.MULTIPLICATION)
			return a * b;
		else if (op == Operation.DIVISION)
			return a / b;
		//NOTE
		/*
		 * If the first argument is finite and less than zero and
		 * If the second argument is finite and not an integer, then the result is NaN.
		 */
		else if (op == Operation.EXPONENTIATION)
			return Math.pow(a, b);
		else if (op == Operation.LOGARITHMN)
			return Math.log(a);
		else if (op == Operation.LOGARITHM10)
			return Math.log10(a);
		else if (op == Operation.SINE)
			return Math.sin(a);
		else if (op == Operation.ARCSINE)
			return Math.asin(a);
		else if (op == Operation.COSINE)
			return Math.cos(a);
		else if (op == Operation.ARCCOSINE)
			return Math.acos(a);
		else if (op == Operation.TANGENT)
			return Math.tan(a);
		else if (op == Operation.ARCTANGENT)
			return Math.atan(a);
		throw new IllegalArgumentException();
	}
	
	public static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return evaluate(s);
		}
	}
}

enum Operation {
	ADDITION,
	SUBTRACTION,
	MULTIPLICATION,
	DIVISION,
	EXPONENTIATION,
	LOGARITHMN, //natural log
	LOGARITHM10, //base 10 log
	SINE, //trigonometry
	COSINE,
	TANGENT,
	ARCSINE, //inverses
	ARCCOSINE,
	ARCTANGENT
}
