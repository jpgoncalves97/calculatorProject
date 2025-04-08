package com.example.calculator.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class CalculatorRequest {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("a")
    private String a;

    @JsonProperty("b")
    private String b;


    public CalculatorRequest(String type, String a, String b){
        this.uuid = UUID.randomUUID().toString();
        this.type = type;
        this.a = a;
        this.b = b;
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
