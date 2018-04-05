package com.lukas.er.monitoring.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukas.er.monitoring.entity.AverageRate;
import com.lukas.er.monitoring.entity.BuyAndSellRate;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class JsonObjectReaderServiceImpl implements JsonObjectReaderService{

    JSONParser parser;
    ObjectMapper mapper;


    public JsonObjectReaderServiceImpl()
    {
        parser = new JSONParser();
        mapper = new ObjectMapper();
    }

    public AverageRate getAverageRate(String filePath, String fileName) throws IOException, ParseException {

        Object obj = parser.parse(new FileReader(filePath+fileName));
        JSONArray jsonArray = (JSONArray) obj;
        AverageRate table = mapper.readValue(jsonArray.get(0).toString(), AverageRate.class);
        table.setFileName(fileName);
        return table;
    }

    public BuyAndSellRate getBuyAndSellRate(String filePath, String fileName) throws IOException, ParseException {

        Object obj = parser.parse(new FileReader(filePath+fileName));
        JSONArray jsonArray = (JSONArray) obj;
        BuyAndSellRate table = mapper.readValue(jsonArray.get(0).toString(), BuyAndSellRate.class);
        table.setFileName(fileName);
        return table;
    }
}
