package org.sevenhills.liueri19;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {
	public static final String VERSION = "1.1";
	
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
					subExpression = Double.toString(evaluate(subExpression));
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
			if (Character.isDigit(ch) || ch == '.')
				tempResultString += ch;
			//parse for constants
			else if (ch == 'e') {
				tempResultString += Double.toString(Math.E);
			}
			else if (ch == '+') {
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.ADDITION);
			}
			else if (ch == '-') {
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
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.MULTIPLICATION);
			}
			else if (ch == '/') {
				number = Double.parseDouble(tempResultString);
				tempResultString = "";
				numbers.add(number);
				operations.add(Operation.DIVISION);
			}
			else if (ch == '^') {
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
		else if (op == Operation.EXPONENTIATION)
			return Math.pow(a, b);
		throw new IllegalArgumentException();
	}
}

enum Operation {
	ADDITION,
	SUBTRACTION,
	MULTIPLICATION,
	DIVISION,
	EXPONENTIATION;
}
