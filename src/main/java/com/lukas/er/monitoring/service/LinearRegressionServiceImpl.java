package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.RateDataDto;
import com.lukas.er.monitoring.dto.TradingRateDataDto;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class LinearRegressionServiceImpl implements LinearRegressionService {

    @Value("${extreme.robotics.predictionNumberOfDays}")
    private int predictionNumberOfDays;


    public List<RateDataDto> calculateAverageRatelinearRegression(List<RateDataDto> rateDataDtoList) throws ParseException {
        SimpleRegression simpleRegressionMid = new SimpleRegression();

     RateDataDto rateDataDto = rateDataDtoList.get(rateDataDtoList.size() - 1);

        for (int x = 1; x < (predictionNumberOfDays+1); x++) {

            double[][] historicalData = new double[rateDataDtoList.size()][2];
            int indexOfArray = 1;

            for (int i = 0; i < rateDataDtoList.size(); i++) {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        historicalData[i][j] = indexOfArray;
                    } else {
                        historicalData[i][j] = Double.parseDouble(rateDataDtoList.get(i).getMid());
                    }
                }
                indexOfArray++;
            }

            simpleRegressionMid.addData(historicalData);
            rateDataDtoList.add(new RateDataDto(Date.valueOf(addNumberOfDaysToCurrentDate(x))
                    , rateDataDto.getCurrency(),
                    rateDataDto.getCode(), String.valueOf(simpleRegressionMid.predict(x))));
        }

        return rateDataDtoList;
    }

    public List<TradingRateDataDto> calculateTradingRateLinearRegression(List<TradingRateDataDto> tradingRateDataDtoList) throws ParseException {
        SimpleRegression simpleRegressionBid = new SimpleRegression();
        SimpleRegression simpleRegressionAsk = new SimpleRegression();

        TradingRateDataDto tradingRateDataDto = tradingRateDataDtoList.get(tradingRateDataDtoList.size() - 1);

        for (int x = 1; x < (predictionNumberOfDays+1); x++) {

            double[][] historicalDataBid = new double[tradingRateDataDtoList.size()][2];
            double[][] historicalDataAsk = new double[tradingRateDataDtoList.size()][2];
            int indexOfArray = 1;

            for (int i = 0; i < tradingRateDataDtoList.size(); i++) {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        historicalDataBid[i][j] = indexOfArray;
                        historicalDataAsk[i][j] = indexOfArray;
                    } else {
                        historicalDataBid[i][j] = Double.parseDouble(tradingRateDataDtoList.get(i).getBid());
                        historicalDataAsk[i][j] = Double.parseDouble(tradingRateDataDtoList.get(i).getAsk());

                    }
                }
                indexOfArray++;
            }

            simpleRegressionBid.addData(historicalDataBid);
            simpleRegressionAsk.addData(historicalDataAsk);

            tradingRateDataDtoList.add(new TradingRateDataDto(Date.valueOf(addNumberOfDaysToCurrentDate(x))
                    ,tradingRateDataDto.getCurrency(), tradingRateDataDto.getCode(),
                    String.valueOf(simpleRegressionBid.predict(x)),String.valueOf(simpleRegressionAsk.predict(x))));
        }

        return tradingRateDataDtoList;
    }


    public String addNumberOfDaysToCurrentDate(int i) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, i);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");



        return format1.format(cal.getTime());
    }


    public void setPredictionNumberOfDays(int predictionNumberOfDays) {
        this.predictionNumberOfDays = predictionNumberOfDays;
    }

}
