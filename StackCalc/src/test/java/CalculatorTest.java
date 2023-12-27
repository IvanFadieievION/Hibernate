import main.Calculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    private static Calculator calculator;

    @BeforeAll
    static void setUp() {
        calculator = new Calculator();
    }

    @Test
    void addition_two_operands() {
        int actual = calculator.compute("1+2");
        int expected = 3;
        assertEquals(actual, expected);
    }

    @Test
    void addition_three_operands() {
        int actual = calculator.compute("1+2+3");
        int expected = 6;
        assertEquals(actual, expected);
    }

    @Test
    void addition_decimal_operands() {
        int actual = calculator.compute("10+2");
        int expected = 12;
        assertEquals(actual, expected);
    }

    @Test
    void subtraction_two_operands() {
        int actual = calculator.compute("2-1");
        int expected = 1;
        assertEquals(actual, expected);
    }

    @Test
    void subtraction_three_operands() {
        int actual = calculator.compute("3-2-1");
        int expected = 0;
        assertEquals(actual, expected);
    }

    @Test
    void subtraction_decimal_operands() {
        int actual = calculator.compute("1000-500");
        int expected = 500;
        assertEquals(actual, expected);
    }

    @Test
    void multiplying_two_operands() {
        int actual = calculator.compute("1*2");
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    void multiplying_three_operands() {
        int actual = calculator.compute("1*2*3");
        int expected = 6;
        assertEquals(actual, expected);
    }

    @Test
    void multiplying_decimal_operands() {
        int actual = calculator.compute("10*5");
        int expected = 50;
        assertEquals(actual, expected);
    }

    @Test
    void division_two_operands() {
        int actual = calculator.compute("2/1");
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    void division_three_operands() {
        int actual = calculator.compute("8/2/2");
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    void division_decimal_operands() {
        int actual = calculator.compute("100/2");
        int expected = 50;
        assertEquals(actual, expected);
    }

    @Test
    void parenthesis_first_calculation_() {
        int actual = calculator.compute("(100+50)/5");
        int expected = 30;
        assertEquals(actual, expected);
    }

    @Test
    void parenthesis_last_calculation() {
        int actual = calculator.compute("10*(4+6)");
        int expected = 100;
        assertEquals(actual, expected);
    }

    @Test
    void parenthesis_inner_calculation() {
        int actual = calculator.compute("(10*(4+6))+20");
        int expected = 120;
        assertEquals(actual, expected);
    }

    @Test
    void negative_calculation_one_negative() {
        int actual = calculator.compute("5-10");
        int expected = -5;
        assertEquals(actual, expected);
    }

    @Test
    void negative_calculation_two_negative() {
        int actual = calculator.compute("-5-1");
        int expected = -6;
        assertEquals(actual, expected);
    }

    @Test
    void minus_minus_calculation() {
        int actual = calculator.compute("5-(-10)");
        int expected = 15;
        assertEquals(actual, expected);
    }

    @Test
    void plus_minus_calculation() {
        int actual = calculator.compute("5+(-10)");
        int expected = -5;
        assertEquals(actual, expected);
    }

    @Test
    void division_by_zero_last() {
        assertThrows(ArithmeticException.class, () -> calculator.compute("10/0"),
                "Can't divide by zero");
    }

    @Test
    void division_by_zero_first() {
        assertThrows(ArithmeticException.class, () -> calculator.compute("0/10"),
                "Can't divide by zero");
    }

    @Test
    void division_by_random() {
        assertThrows(ArithmeticException.class, () -> calculator.compute("5/(2-2)"),
                "Can't divide by zero");
    }

    @Test
    void operator_precedence_calculation() {
        int actual = calculator.compute("8-6/2");
        int expected = 5;
        assertEquals(actual, expected);
    }

    @Test
    void empty_input() {
        assertThrows(InputMismatchException.class, () -> calculator.compute(""));
    }

    @Test
    void no_operands() {
        assertThrows(InputMismatchException.class, () -> calculator.compute("()"));
    }

    @Test
    void single_operand() {
        int actual = calculator.compute("1");
        int expected = 1;
        assertEquals(actual, expected);
    }

    @Test
    void first_random_calculation() {
        int actual = calculator.compute("5*(2+4)-8/(3+1)");
        int expected = 28;
        assertEquals(actual, expected);
    }

    @Test
    void second_random_calculation() {
        int actual = calculator.compute("123+456*789-987");
        int expected = 358920;
        assertEquals(actual, expected);
    }

    @Test
    void third_random_calculation() {
        int actual = calculator.compute("(11+18)*20-2");
        int expected = 578;
        assertEquals(actual, expected);
    }

    @Test
    void fourth_random_calculation() {
        int actual = calculator.compute("10+2*6/2-7");
        int expected = 9;
        assertEquals(actual, expected);
    }
}
