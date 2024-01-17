package main;

import java.util.InputMismatchException;
import java.util.Stack;

public class Calculator {

    public int compute(String inFixExpression) {
        String postFixExpression = inFixToPostFix(inFixExpression);
        Stack<Integer> operands = new Stack<>();
        StringBuilder operand = new StringBuilder();

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

                    Integer first = operands.pop();
                    Integer second = operands.pop();

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
        if (operands.empty()) {
            throw new InputMismatchException();
        }
        return operands.pop();
    }

    private String inFixToPostFix(String infixExpression) {
        checkForParenthesis(infixExpression);

        StringBuilder postFixExpression = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < infixExpression.length(); i++) {
            char ch = infixExpression.charAt(i);
            if (Character.isDigit(ch)) {
                i = addDigitsToOperand(infixExpression, i, postFixExpression);
            } else if (ch == '-') {
                handleMinusOperator(operatorStack, ch, postFixExpression, i, infixExpression);
            } else {
                if (operatorStack.empty() || ch == '(') {
                    operatorStack.push(ch);
                } else if (ch == ')') {
                    handleClosingParenthesisOperator(operatorStack, postFixExpression);
                } else {
                    handleOperator(ch, operatorStack, postFixExpression);
                }
            }
        }
        addOperatorsToPostFixExpression(operatorStack, postFixExpression);
        return postFixExpression.toString();
    }

    private void checkForParenthesis(String infixExpression) {
        if (infixExpression.contains("(") && !infixExpression.contains(")")
                || infixExpression.contains(")") && !infixExpression.contains("(")) {
            throw new RuntimeException("Parenthesis missing");
        }
    }

    private int appendDigitsToOperand(int i, String postFixExpression, StringBuilder operand) {
        int start = i;
        while (Character.isDigit(postFixExpression.charAt(i))) {
            i++;
        }
        operand.append(postFixExpression, start, i);
        i--;
        return i;
    }

    private void pushOperandToStack(Stack<Integer> operands, StringBuilder operand) {
        operands.push(Integer.parseInt(operand.toString()));
        operand.setLength(0);
    }

    private int addDigitsToOperand(String infixExpression, int i, StringBuilder postFixExpression) {
        while (i < infixExpression.length() && Character.isDigit(infixExpression.charAt(i))) {
            postFixExpression.append(infixExpression.charAt(i));
            i++;
        }
        postFixExpression.append(" ");
        i--;
        return i;
    }

    private void addOperatorsToPostFixExpression(Stack<Character> operatorStack, StringBuilder postFixExpression) {
        while (!operatorStack.empty()) {
            if (postFixExpression.isEmpty()) {
                throw new InputMismatchException("Operands missing");
            }
            postFixExpression.append(operatorStack.pop());
        }
    }

    private void handleOperator(char ch, Stack<Character> operatorStack, StringBuilder postFixExpression) {
        if (getOperatorPrecedence(ch)
                > getOperatorPrecedence(operatorStack.peek())) {
            operatorStack.push(ch);
        } else {
            while (!operatorStack.empty() && getOperatorPrecedence(ch)
                    <= getOperatorPrecedence(operatorStack.peek())) {
                postFixExpression.append(operatorStack.pop());
            }
            operatorStack.push(ch);
        }
    }

    private void handleMinusOperator(Stack<Character> operatorStack, char ch, StringBuilder postFixExpression, int i, String infixExpression) {
        if (isUnaryMinus(infixExpression, i)) {
            postFixExpression.append("#");
        } else if (!operatorStack.empty() && getOperatorPrecedence(ch)
                <= getOperatorPrecedence(operatorStack.peek())) {
            postFixExpression.append(operatorStack.pop());
            operatorStack.push(ch);
        } else {
            operatorStack.push(ch);
        }
    }

    private boolean isUnaryMinus(String infixExpression, int i) {
        if (i == 0) {
            return infixExpression.charAt(i + 1) != '(';
        }
        return infixExpression.charAt(i - 1) == '(' || isOperator(infixExpression.charAt(i - 1));
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private void handleClosingParenthesisOperator(Stack<Character> operatorStack, StringBuilder postFixExpression) {
        while (!operatorStack.empty() && operatorStack.peek() != '(') {
            postFixExpression.append(operatorStack.pop());
        }
        operatorStack.pop();
    }

    private int getOperatorPrecedence (char operator) {
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

    private void checkForDoubleMinus(StringBuilder operand) {
        if (operand.toString().equals("--")) {
            operand.setLength(0);
        }
    }

    private void checkForLastOperator(Stack<Integer> operands, char ch) {
        operands.push(operands.pop()
                * Character.getNumericValue(ch + '1'));
    }
}
