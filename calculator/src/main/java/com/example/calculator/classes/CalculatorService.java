package com.example.calculator.classes;


import com.example.calculator.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculatorService {

    public static final int maxDecimalPlaces = 30;
    private final KafkaProducer producer;

    @Autowired
    CalculatorService(KafkaProducer producer) {
        this.producer = producer;
    }

    @KafkaListener(topics = "calculator-request")
    public void consume(String requestJSON) throws IOException {
        log.info("Consume topic[calculator-request]: {}", requestJSON);
        ObjectMapper mapper = new ObjectMapper();
        CalculatorRequest request = mapper.readValue(requestJSON, CalculatorRequest.class);

        String finalResult;
        try {
            BigDecimal result = getResult(request);
            finalResult = result.toString();
        } catch (ArithmeticException e) {
            finalResult = e.getMessage();
        }
        CalculatorResult calculatorResult = new CalculatorResult(request.getUuid(), finalResult);
        this.producer.sendMessage("calculator-result", calculatorResult.toString());
    }

    public static BigDecimal getResult(CalculatorRequest request) throws ArithmeticException {
        BigDecimal result = new BigDecimal("0.0").setScale(maxDecimalPlaces, RoundingMode.HALF_UP);
        BigDecimal a = new BigDecimal(request.getA()).setScale(maxDecimalPlaces, RoundingMode.HALF_UP);
        BigDecimal b = new BigDecimal(request.getB()).setScale(maxDecimalPlaces, RoundingMode.HALF_UP);
        result = switch (request.getType()) {
            case "sum" -> a.add(b);
            case "subtraction" -> a.subtract(b);
            case "multiplication" -> a.multiply(b);
            case "division" -> a.divide(b, RoundingMode.DOWN).setScale(maxDecimalPlaces);
            default -> result;
        };
        return result.stripTrailingZeros();
    }
}