package com.example.calculator.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class CalculatorResult {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("result")
    public String result;

    public CalculatorResult(){

    }

    public CalculatorResult(String uuid, String result){
        this.uuid = uuid;
        this.result = result;
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
