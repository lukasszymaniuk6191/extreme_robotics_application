package com.lukas.er;

import com.lukas.er.monitoring.dto.RateDataDto;
import com.lukas.er.monitoring.dto.TradingRateDataDto;
import com.lukas.er.monitoring.service.LinearRegressionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LinearRegressionServiceTest {

    @Autowired
    private LinearRegressionService linearRegressionService;

    @Test
    public void calculateAverageRateLinearRegressionTest() throws ParseException {
        linearRegressionService.setPredictionNumberOfDays(2);
        List<RateDataDto> rateDataDtoList = new ArrayList<>();
        rateDataDtoList.add(RateDataDto.builder().tableDate(Date.valueOf("2018-03-28"))
                .currency("dolar amerykański").code("USD").mid("3.367").build());
        rateDataDtoList.add(RateDataDto.builder().tableDate(Date.valueOf("2018-03-29"))
                .currency("dolar amerykański").code("USD").mid("3.382").build());

        assertEquals(rateDataDtoList.size(), 2);

        linearRegressionService.calculateAverageRatelinearRegression(rateDataDtoList);
        assertEquals(rateDataDtoList.size(), 4);

        linearRegressionService.setPredictionNumberOfDays(10);
        linearRegressionService.calculateAverageRatelinearRegression(rateDataDtoList);
        assertEquals(rateDataDtoList.size(), 14);
    }

    @Test
    public void calculateTradingRateLinearRegressionTest() throws ParseException {
        linearRegressionService.setPredictionNumberOfDays(5);
        List<TradingRateDataDto> tradingRateDataDtoList = new ArrayList<>();
        tradingRateDataDtoList.add(TradingRateDataDto.builder().tableDate(Date.valueOf("2018-03-28"))
                .currency("dolar amerykański").code("USD").bid("3.367").ask("3.435").build());
        tradingRateDataDtoList.add(TradingRateDataDto.builder().tableDate(Date.valueOf("2018-03-29"))
                .currency("dolar amerykański").code("USD").bid("3.382").ask("2.543").build());

        assertEquals(tradingRateDataDtoList.size(), 2);

        linearRegressionService.calculateTradingRateLinearRegression(tradingRateDataDtoList);
        assertEquals(tradingRateDataDtoList.size(), 7);

        linearRegressionService.setPredictionNumberOfDays(13);
        linearRegressionService.calculateTradingRateLinearRegression(tradingRateDataDtoList);
        assertEquals(tradingRateDataDtoList.size(), 20);
    }


}
