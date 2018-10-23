package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/
		StringTokenizer token = new StringTokenizer(expr, delims, true); // tokenizes string
		String prev = token.nextToken(); // prevtoken is set to token.
		String current = ""; 
		
		if(!token.hasMoreTokens()) 
		{
			for (int i = 0; i < prev.length(); i++) // loops through each char of string
			{	
				if (Character.isLetter(prev.charAt(i))) // if the token had any chars, then it is put in var array
				{
					Variable variableInExpr = new Variable(prev);

					if (!(vars.contains(variableInExpr))) {
						vars.add(variableInExpr);
					}
				}
			}
		}
			else {
					current = token.nextToken();
	
		while (token.hasMoreTokens()) {
			if ((current.equals("["))) {
				for (int i = 0; i < prev.length(); i++) // loops through each char of string
				{
					if (Character.isLetter(prev.charAt(i))) // if the token had any chars, then it is put in var array
					{
						Array arrsInExpr = new Array(prev);

						if (!(arrays.contains(arrsInExpr))) {
							arrays.add(arrsInExpr);
						}
					}
				}
			}

			else {	
				for (int i = 0; i < prev.length(); i++) // loops through each char of string
				{	
					if (Character.isLetter(prev.charAt(i))) // if the token had any chars, then it is put in var array
					{
						Variable variableInExpr = new Variable(prev);

						if (!(vars.contains(variableInExpr))) {
							vars.add(variableInExpr);
						}
					}
				}

			}

			prev = current;
			current = token.nextToken();

		}

		for (int i = 0; i < current.length(); i++) // loops through each char of string
		{
			if (Character.isLetter(current.charAt(i))) // if the token had any chars, then it is put in var array
			{
				Variable variableInExpr = new Variable(current);

				if (!(vars.contains(variableInExpr))) {
					vars.add(variableInExpr);
				}
			}
		}
	}

		System.out.println("this is the variables array list: " + vars);
		System.out.println("this is the array array list: " + arrays);
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	private static float performOp(float a, float b, char operator) {
		
		switch (operator) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '/':
			return a / b;
		case '*':
			return a * b;
		}

		return 0;

	}

	private static boolean checkPrecedence(char op1, char op2) {
		if ((op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/')) {
			return true;
		}
		return false;
	}

	private static int findClosingLocation(String expr) {
		int count = 0;
		int closeLocation = 0;

		for (int i = 0; i < expr.length(); i++) {
			if (expr.charAt(i) == '(') {
				count++;
			}
			if (expr.charAt(i) == ')') {
				count--;

				if (count == 0) {
					closeLocation = i;
					return closeLocation;
				}
			}
		}

		return closeLocation;
	}

	private static int findClosingBrac(String expr) {
		int count = 0;
		int closeLocation = 0;

		for (int i = 0; i < expr.length(); i++) {
			if (expr.charAt(i) == '[') {
				count++;
			}
			if (expr.charAt(i) == ']') {
				count--;

				if (count == 0) {
					closeLocation = i;
					return closeLocation;
				}
			}
		}

		return closeLocation;
	}

	/*
	 * 
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		//
		float finalResult = 0;
		float result = 0;
		Stack<Float> operands = new Stack<Float>();
		Stack<Character> operators = new Stack<Character>();
		expr = expr.replaceAll("\\s+", "");
		/*
		 * 
		 * 
		 * 
		 */
		for (int i = 0; i < expr.length(); i++) {
			String num = "";
			String name = "";
			

			while (i < expr.length() && Character.isDigit(expr.charAt(i))) { // if its a number, add each digit to the
																				// string
				// System.out.println("char at i is a num: " + expr.charAt(i));
				num += expr.charAt(i);
				i++;
			}

			// System.out.println("num: " + num);

			if (num.length() > 0) { // push number into the operands stack if the length is greater than zero
				operands.push(Float.parseFloat(num));
				// System.out.println("whats in the stack: " + operands.peek());

			}

			while (i < expr.length() && Character.isLetter(expr.charAt(i))) { // if the char is a letter, put each char
																				// into the string name
				// System.out.println("i is less than the length of the expr and char is a
				// letter: " + expr.charAt(i));
				name += expr.charAt(i);
				i++;

			}

			// System.out.println("full name after looping: " + name);

			if (name.length() > 0) { // if the length of the name is greater than zero
				if (i < expr.length() && expr.charAt(i) == '[') { // if the char after the name is a bracket, then it
																	// must be an array
					Array arrName = new Array(name);
					int endBrac = i + findClosingBrac(expr.substring(i));
					int index = (int) evaluate(expr.substring(i + 1, endBrac), vars, arrays);
					float val = (float) arrays.get(arrays.indexOf(arrName)).values[index]; // push value in the array
					operands.push(val);
					i = endBrac;

				} else {
					Variable varName = new Variable(name);
					Variable var = vars.get(vars.indexOf(varName)); // if its not an array, push the variable value into
					operands.push((float) var.value);
				}
			}
			if (i < expr.length() && expr.charAt(i) != '(' && expr.charAt(i) != ')' && expr.charAt(i) != '[' && expr.charAt(i) != ']') { // if it wasnt a var, array, or
				// number, and also not a paren,
				// then it must
				// be an operator
				if (!operators.isEmpty()) {
					if (checkPrecedence(expr.charAt(i), operators.peek())) {
						float num1 = operands.pop();
						float num2 = operands.pop();
						char operator = operators.pop();
						float res = performOp(num2, num1, operator);
						operands.push(res);
					}
					operators.push(expr.charAt(i)); // no precedence, stack was empty, immediately push
				} else {
					operators.push(expr.charAt(i));
				}

			} else {
				if (i < expr.length() && expr.charAt(i) == '(') { // if its a round paren,
					int endParen = i + findClosingLocation(expr.substring(i)); // find index of closing
					float f = evaluate(expr.substring(i + 1, endParen), vars, arrays); // eval substring discluding
					operands.push(f); // push the value into the operands stack
					i = endParen; // increment i to after the closing paren
				}
			}
		} // end of for loop
		
		Stack<Float> reverseNums = new Stack<Float>();
		Stack<Character> reverseOps = new Stack<Character>();
		
		if(operators.size() >= 2) {
			char operator = operators.pop();
			float num1 = operands.pop();
			float num2 = operands.pop();
			if (checkPrecedence(operators.peek(), operator)) {
				float res = performOp(num2, num1, operator);
				operands.push(res);
			}
			else {
				operands.push(num2);
				operands.push(num1);
				operators.push(operator);
			}
		}

			

		while (!(operands.isEmpty())) {
			reverseNums.push(operands.pop());

		}
		while (!(operators.isEmpty())) {
			reverseOps.push(operators.pop());
		}

		while (!(reverseOps.isEmpty())) {
			float number1 = reverseNums.pop();
			float number2 = reverseNums.pop();
			char op = reverseOps.pop();
			result = performOp(number1, number2, op);
			reverseNums.push(result);

		}

		finalResult = reverseNums.pop();
		return finalResult;
	}

}
