package com.lukas.er.monitoring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/traiding_rates")
    public String traidingRates() {
        return "buy_and_sell_rates.html";
    }

    @GetMapping("/average_rates")
    public String averageRates() {
        return "average_rates.html";
    }


}
