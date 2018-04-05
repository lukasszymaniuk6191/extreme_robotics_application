package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.entity.AverageRate;
import com.lukas.er.monitoring.entity.BuyAndSellRate;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface JsonObjectReaderService {

    AverageRate getAverageRate(String filePath, String fileName) throws IOException, ParseException;

    BuyAndSellRate getBuyAndSellRate(String filePath, String fileName) throws IOException, ParseException;

}
