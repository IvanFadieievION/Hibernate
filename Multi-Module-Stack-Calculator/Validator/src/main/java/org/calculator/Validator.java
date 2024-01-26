package org.calculator;

import java.util.Stack;

public class Validator {
    public static void validate(String infixExpression) {
        Stack<Character> parenthesis = new Stack<>();
        for (char ch : infixExpression.toCharArray()) {
            if (ch == '(') {
                parenthesis.push(ch);
            } else if (ch == ')' && parenthesis.empty()) {
                throw new RuntimeException("Invalid parentheses");
            } else if (ch == ')' && parenthesis.peek() == '(') {
                parenthesis.pop();
            }
        }
        if (!parenthesis.empty()) {
            throw new RuntimeException("Invalid parentheses");
        }
    }
}
