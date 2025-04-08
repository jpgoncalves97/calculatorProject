package com.example.calculator;

import com.example.calculator.classes.CalculatorRequest;
import com.example.calculator.classes.CalculatorService;
import com.example.calculator.classes.KafkaListenersTypeExcludeFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TypeExcludeFilters(KafkaListenersTypeExcludeFilter.class)
@SpringBootTest(classes = CalculatorApplicationTests.class)
class CalculatorApplicationTests {

	static void assertEqualDecimals(String actual, BigDecimal expected){
		assertEquals(new BigDecimal(actual).stripTrailingZeros(), expected.stripTrailingZeros());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testSum(){
		BigDecimal result1 = CalculatorService.getResult(new CalculatorRequest("sum", "1.0", "2.0"));
		BigDecimal result2 = CalculatorService.getResult(new CalculatorRequest("sum", "1.000000000000001", "2.0"));
		BigDecimal result3 = CalculatorService.getResult(new CalculatorRequest("sum", "1.0", "-3.000000000000001"));
		BigDecimal result4 = CalculatorService.getResult(new CalculatorRequest("sum", "111111111111111111111111", "222222222222222222222222"));
		assertEqualDecimals("3.0",result1);
		assertEqualDecimals("3.000000000000001", result2);
		assertEqualDecimals("-2.000000000000001", result3);
		assertEqualDecimals("333333333333333333333333", result4);
	}

	@Test
	void testSubtraction(){
		BigDecimal result1 = CalculatorService.getResult(
				new CalculatorRequest("subtraction", "1.0", "2.0")
		);
		BigDecimal result2 = CalculatorService.getResult(
				new CalculatorRequest("subtraction", "1.000000000000001", "1.0")
		);

		assertEqualDecimals("-1.0", result1);
		assertEqualDecimals("0.000000000000001", result2);
	}

	@Test
	void testMultiplication(){
		BigDecimal result1 = CalculatorService.getResult(
				new CalculatorRequest("multiplication", "-2.0", "-2.0")
		);
		BigDecimal result2 = CalculatorService.getResult(
				new CalculatorRequest("multiplication", "1e100", "1e100")
		);

		assertEqualDecimals("4",result1);
		assertEqualDecimals("1e200", result2);
	}

	@Test
	void testDivision(){
		BigDecimal result1 = CalculatorService.getResult(
				new CalculatorRequest("division", "-2.0", "-2.0")
		);

		BigDecimal result2 = CalculatorService.getResult(
				new CalculatorRequest("division", "1e100", "1e100")
		);
		BigDecimal result3 = CalculatorService.getResult(
				new CalculatorRequest("division", "1", "3")
		);

		assertEqualDecimals("1",result1);
		assertEqualDecimals("1", result2);
		assertEqualDecimals("0.333333333333333333333333333333", result3);
	}

	@Test
	void testDivideByZeroThrowsException() {
		ArithmeticException exception = assertThrows(ArithmeticException.class,
				() -> CalculatorService.getResult(
						new CalculatorRequest("division", "-2.0", "0")
				)
		);
		assertEquals("BigInteger divide by zero", exception.getMessage());
	}

	@Test
	void testMaxDecimalPlaces(){
		BigDecimal result = CalculatorService.getResult(
				new CalculatorRequest("division", "1", "3")
		);
		assertEquals(CalculatorService.maxDecimalPlaces, result.scale());
	}

}
