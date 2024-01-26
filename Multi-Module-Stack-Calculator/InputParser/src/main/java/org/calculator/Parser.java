package org.calculator;

import java.util.InputMismatchException;
import java.util.Stack;

import static org.calculator.Validator.validate;

public class Parser {
    private static final StringBuilder postFixExpression = new StringBuilder();
    private static final Stack<Character> operators = new Stack<>();
    public static String inFixToPostFix(String infixExpression) {
        validate(infixExpression);
        for (int i = 0; i < infixExpression.length(); i++) {
            char ch = infixExpression.charAt(i);
            if (Character.isDigit(ch)) {
                i = addDigitsToOperand(infixExpression, i);
            } else if (ch == '-') {
                handleMinusOperator(ch, i, infixExpression);
            } else {
                if (operators.empty() || ch == '(') {
                    operators.push(ch);
                } else if (ch == ')') {
                    handleClosingParenthesisOperator();
                } else {
                    handleOperator(ch);
                }
            }
        }
        addOperatorsToPostFixExpression();
        return postFixExpression.toString();
    }

    private static int addDigitsToOperand(String infixExpression, int i) {
        while (i < infixExpression.length() && Character.isDigit(infixExpression.charAt(i))) {
            postFixExpression.append(infixExpression.charAt(i));
            i++;
        }
        postFixExpression.append(" ");
        i--;
        return i;
    }

    private static void addOperatorsToPostFixExpression() {
        while (!Parser.operators.empty()) {
            if (postFixExpression.isEmpty()) {
                throw new InputMismatchException("Operands missing");
            }
            postFixExpression.append(Parser.operators.pop());
        }
    }

    private static void handleMinusOperator(char ch, int i, String infixExpression) {
        if (isUnaryMinus(infixExpression, i)) {
            Parser.postFixExpression.append("#");
        } else if (!Parser.operators.empty() && getOperatorPrecedence(ch)
                <= getOperatorPrecedence(Parser.operators.peek())) {
            Parser.postFixExpression.append(Parser.operators.pop());
            Parser.operators.push(ch);
        } else {
            Parser.operators.push(ch);
        }
    }

    private static boolean isUnaryMinus(String infixExpression, int i) {
        if (i == 0) {
            return infixExpression.charAt(i + 1) != '(';
        }
        return infixExpression.charAt(i - 1) == '(' || isOperator(infixExpression.charAt(i - 1));
    }

    private static void handleOperator(char ch) {
        if (getOperatorPrecedence(ch)
                > getOperatorPrecedence(Parser.operators.peek())) {
            Parser.operators.push(ch);
        } else {
            while (!Parser.operators.empty() && getOperatorPrecedence(ch)
                    <= getOperatorPrecedence(Parser.operators.peek())) {
                Parser.postFixExpression.append(Parser.operators.pop());
            }
            Parser.operators.push(ch);
        }
    }

    private static void handleClosingParenthesisOperator() {
        while (!Parser.operators.empty() && Parser.operators.peek() != '(') {
            Parser.postFixExpression.append(Parser.operators.pop());
        }
        Parser.operators.pop();
    }

    private static int getOperatorPrecedence (char operator) {
        if (operator == '-') {
            return 1;
        } else if (operator == '+') {
            return 2;
        } else if (operator == '/') {
            return 3;
        } else if (operator == '*') {
            return 4;
        } else {
            return -1;
        }
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    public static void clearPostFixExpression() {
        postFixExpression.setLength(0);
    }
}
