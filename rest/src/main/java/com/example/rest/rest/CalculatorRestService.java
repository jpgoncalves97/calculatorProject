package com.example.rest.rest;

import com.example.rest.classes.CalculatorRequest;
import com.example.rest.classes.CalculatorResult;
import com.example.rest.classes.Pair;
import com.example.rest.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CalculatorRestService {

    private final KafkaProducer producer;

    HashMap<String, CompletableFuture<Pair>> futures;

    @Autowired
    CalculatorRestService(KafkaProducer producer) {
        this.producer = producer;
        this.futures = new HashMap<>();
    }

    @KafkaListener(topics = "calculator-result", groupId = "foo")
    public void consume(String message) throws IOException {
        log.info("Consume topic[calculator-result]: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        CalculatorResult u = mapper.readValue(message, CalculatorResult.class);

        this.futures.get(u.uuid).complete(new Pair(u.uuid, "{ \"result\": \"" + u.result + "\" }"));
    }

    private Pair getFutureResult(CalculatorRequest request){
        futures.put(request.getUuid(), new CompletableFuture<>());
        this.producer.sendMessage("calculator-request", request.toString());
        return futures.get(request.getUuid()).join();
    }

    public Pair sum(String a, String b) {
        return getFutureResult(new CalculatorRequest("sum", a, b));
    }

    public Pair subtraction(String a, String b) {
        return getFutureResult(new CalculatorRequest("subtraction", a, b));
    }

    public Pair multiplication(String a, String b) {
        return getFutureResult(new CalculatorRequest("multiplication", a, b));
    }

    public Pair division(String a, String b)  {
        return getFutureResult(new CalculatorRequest("division", a, b));
    }
}
