package com.example.rest.rest;

import com.example.rest.classes.Pair;
import com.example.rest.validators.ValidNumber;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Validated
@RestController
public class CalculatorRestController {

    private final Logger logger = LoggerFactory.getLogger(CalculatorRestController.class);
    private final CalculatorRestService service;

    HashMap<String, CompletableFuture<String>> futures;

    @Autowired
    CalculatorRestController(CalculatorRestService service) {
        this.service = service;
    }

    @GetMapping(value = "/sum")
    public String sum(@ValidNumber @RequestParam("a") String a, @ValidNumber @RequestParam("b") String b, HttpServletResponse response)
            throws ConstraintViolationException {

        Pair p = service.sum(a, b);
        response.setHeader("uuid", p.uuid());
        return p.result();
    }

    @GetMapping(value = "/subtraction")
    public String subtraction(@Valid @RequestParam("a") String a, @Valid @RequestParam("b") String b, HttpServletResponse response)
            throws ConstraintViolationException {
        Pair p = service.subtraction(a, b);
        response.setHeader("uuid", p.uuid());
        return p.result();
    }

    @GetMapping(value = "/multiplication")
    public String multiplication(@Valid @RequestParam("a") String a, @Valid @RequestParam("b") String b, HttpServletResponse response)
            throws ConstraintViolationException{
        Pair p = service.multiplication(a, b);
        response.setHeader("uuid", p.uuid());
        return p.result();
    }

    @GetMapping(value = "/division")
    public String division(@Valid @RequestParam("a") String a, @Valid @RequestParam("b") String b, HttpServletResponse response)
            throws ConstraintViolationException{

        Pair p = service.division(a, b);
        response.setHeader("uuid", p.uuid());
        return p.result();
    }

}
