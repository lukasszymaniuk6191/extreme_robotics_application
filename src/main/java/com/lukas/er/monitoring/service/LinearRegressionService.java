package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.RateDataDto;
import com.lukas.er.monitoring.dto.TradingRateDataDto;

import java.text.ParseException;
import java.util.List;

public interface LinearRegressionService {
    List<RateDataDto> calculateAverageRatelinearRegression(List<RateDataDto> rateDataDtoList) throws ParseException;
    List<TradingRateDataDto> calculateTradingRateLinearRegression(List<TradingRateDataDto> tradingRateDataDtoList) throws ParseException;
    void setPredictionNumberOfDays(int predictionNumberOfDays);
}
