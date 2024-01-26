package org.calculator;

import java.util.InputMismatchException;
import java.util.Stack;

import static org.calculator.Parser.inFixToPostFix;

public class Evaluator {
    public static int compute(String inFixExpression) {
        String postFixExpression = inFixToPostFix(inFixExpression);
        StringBuilder operand = new StringBuilder();
        Stack<Integer> operands = new Stack<>();

        for (int i = 0; i < postFixExpression.length(); i++) {
            char ch = postFixExpression.charAt(i);
            if (Character.isDigit(ch)) {
                i = appendDigitsToOperand(i, postFixExpression, operand);
            } else if (ch == ' ' && !operand.isEmpty()) {
                pushOperandToStack(operands, operand);
            } else if (ch == '#') {
                operand.append('-');
                checkForDoubleMinus(operand);
            } else {
                if (!operand.isEmpty()) {
                    pushOperandToStack(operands, operand);
                } else if (operands.size() > 1) {

                    int first = operands.pop();
                    int second = operands.pop();

                    switch (ch) {
                        case '+':
                            operands.push(second + first);
                            break;
                        case '-':
                            operands.push(second - first);
                            break;
                        case '/':
                            if (first == 0 || second == 0) {
                                throw new ArithmeticException("Can't divide by zero");
                            }
                            operands.push(second / first);
                            break;
                        case '*':
                            operands.push(second * first);
                            break;
                    }
                } else {
                    checkForLastOperator(operands, ch);
                }
            }
        }
        if (operands.isEmpty()) {
            throw new InputMismatchException();
        }
        return operands.pop();
    }

    private static int appendDigitsToOperand(int i, String postFixExpression, StringBuilder operand) {
        int start = i;
        while (Character.isDigit(postFixExpression.charAt(i))) {
            i++;
        }
        operand.append(postFixExpression, start, i);
        i--;
        return i;
    }

    private static void checkForDoubleMinus(StringBuilder operand) {
        if (operand.toString().equals("--")) {
            operand.setLength(0);
        }
    }

    private static void pushOperandToStack(Stack<Integer> operands, StringBuilder operand) {
        operands.push(Integer.parseInt(operand.toString()));
        operand.setLength(0);
    }


    private static void checkForLastOperator(Stack<Integer> operands, char ch) {
        operands.push(operands.pop()
                * Character.getNumericValue(ch + '1'));
    }
}