package com.lukas.er.monitoring.controller;

import com.lukas.er.monitoring.dto.RateDataDto;
import com.lukas.er.monitoring.dto.TradingRateDataDto;
import com.lukas.er.monitoring.repository.AverangeRatesRepository;
import com.lukas.er.monitoring.repository.BuyAndSellRateRepository;
import com.lukas.er.monitoring.service.LinearRegressionService;
import com.lukas.er.monitoring.validator.ValidDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class RestApiController {

    public AverangeRatesRepository averangeRatesRepository;
    public BuyAndSellRateRepository buyAndSellRateRepository;
    public LinearRegressionService linearRegressionService;

    @Autowired
    public RestApiController(AverangeRatesRepository averangeRatesRepository,
                             BuyAndSellRateRepository buyAndSellRateRepository,
                             LinearRegressionService linearRegressionService) {
        this.averangeRatesRepository = averangeRatesRepository;
        this.buyAndSellRateRepository = buyAndSellRateRepository;
        this.linearRegressionService = linearRegressionService;
    }


    @GetMapping(path = "/average_rates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RateDataDto> getAverageRatesByCodeAndDataRange(@Size(min = 3, max = 3) @RequestParam(required = true) String code,
                                                               @ValidDate @RequestParam(required = true) String startDate,
                                                               @ValidDate @RequestParam(required = true) String stopDate) throws ParseException {
        List<RateDataDto> rateDataDtoList = averangeRatesRepository
                .getRateDataByCodeAndTableDateBetween(
                        code, Date.valueOf(startDate), Date.valueOf(stopDate));
        return linearRegressionService.calculateAverageRatelinearRegression(rateDataDtoList);
    }

    //@GetMapping(path = "/current/average_rates/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    @GetMapping("/current/average_rates/all")
    public List<RateDataDto> getCurrentDataAverageRatesAll() {
        List<RateDataDto> rateDataDtoList = averangeRatesRepository
                .getAllAverageRateByDate(Date.valueOf(getCurrentDate()));
        return rateDataDtoList;
    }


    @GetMapping(path = "/trading_rates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TradingRateDataDto> getDataTraidingRatesByCodeAndDataRange(@Size(min = 3, max = 3) @RequestParam(required = true) String code,
                                                                           @ValidDate @RequestParam(required = true) String startDate,
                                                                           @ValidDate @RequestParam(required = true) String stopDate) throws ParseException {
        List<TradingRateDataDto> tradingRateDataDtoList = buyAndSellRateRepository
                .getTradingRatesDataByCodeAndTableDateBetween(
                        code, Date.valueOf(startDate), Date.valueOf(stopDate));
        return linearRegressionService.calculateTradingRateLinearRegression(tradingRateDataDtoList);
    }

    @GetMapping(path = "/current/trading_rates/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TradingRateDataDto> getCurrentDataTraidingRatesAll() {
        List<TradingRateDataDto> tradingRateDataDtoList = buyAndSellRateRepository
                .getAllTraidingRateByDate(Date.valueOf(getCurrentDate()));
        return tradingRateDataDtoList;
    }


    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(cal.getTime());
    }

    public void setAverangeRatesRepository(AverangeRatesRepository averangeRatesRepository) {
        this.averangeRatesRepository = averangeRatesRepository;
    }

    public void setBuyAndSellRateRepository(BuyAndSellRateRepository buyAndSellRateRepository) {
        this.buyAndSellRateRepository = buyAndSellRateRepository;
    }

    public void setLinearRegressionService(LinearRegressionService linearRegressionService) {
        this.linearRegressionService = linearRegressionService;
    }
}
