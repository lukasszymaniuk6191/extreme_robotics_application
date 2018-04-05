package com.lukas.er;

import com.lukas.er.monitoring.controller.RestApiController;
import com.lukas.er.monitoring.dto.RateDataDto;
import com.lukas.er.monitoring.dto.TradingRateDataDto;
import com.lukas.er.monitoring.repository.AverangeRatesRepository;
import com.lukas.er.monitoring.repository.BuyAndSellRateRepository;
import com.lukas.er.monitoring.service.LinearRegressionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Date;
import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AverangeRatesRepository averangeRatesRepositoryMock;
    @Mock
    private BuyAndSellRateRepository buyAndSellRateRepositoryMock;
    @Mock
    private LinearRegressionService linearRegressionServiceMock;

    @Autowired
    public RestApiController restApiController;

    @Before
    public void setUp() {
        restApiController.setAverangeRatesRepository(averangeRatesRepositoryMock);
        restApiController.setBuyAndSellRateRepository(buyAndSellRateRepositoryMock);
        restApiController.setLinearRegressionService(linearRegressionServiceMock);
    }

    @Test
    public void getDataAverageRatesAllCorrectTest() throws Exception {
        RateDataDto rateDataDto_1 = RateDataDto.builder()
                .tableDate(Date.valueOf("2018-04-04"))
                .currency("bat (Tajlandia)")
                .code("THB")
                .mid("0.1096")
                .build();
        RateDataDto rateDataDto_2 = RateDataDto.builder()
                .tableDate(Date.valueOf("2018-04-04"))
                .currency("dolar amerykański")
                .code("USD")
                .mid("3.4177")
                .build();
        when(averangeRatesRepositoryMock.getAllAverageRateByDate(Date.valueOf("2018-04-04")))
                .thenReturn(Arrays.asList(rateDataDto_1, rateDataDto_2));

        mockMvc.perform(get("/api/current/average_rates/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tableDate").value("2018-04-04"))
                .andExpect(jsonPath("$[0].currency").value("bat (Tajlandia)"))
                .andExpect(jsonPath("$[0].code").value("THB"))
                .andExpect(jsonPath("$[0].mid").value("0.1096"))
                .andExpect(jsonPath("$[1].tableDate").value("2018-04-04"))
                .andExpect(jsonPath("$[1].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[1].code").value("USD"))
                .andExpect(jsonPath("$[1].mid").value("3.4177"));

        verify(averangeRatesRepositoryMock, times(1)).getAllAverageRateByDate(Date.valueOf("2018-04-04"));


    }

    @Test
    public void getDataAverageRatesByCodeAndDataRangeCorrectTest() throws Exception {


        RateDataDto rateDataDto_1 = RateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-28"))
                .currency("dolar amerykański")
                .code("USD")
                .mid("3.4145")
                .build();
        RateDataDto rateDataDto_2 = RateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-29"))
                .currency("dolar amerykański")
                .code("USD")
                .mid("3.3956")
                .build();
        RateDataDto rateDataDto_3 = RateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-30"))
                .currency("dolar amerykański")
                .code("USD")
                .mid("3.4139")
                .build();

        when(averangeRatesRepositoryMock.getRateDataByCodeAndTableDateBetween("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30")))
                .thenReturn(Arrays.asList(rateDataDto_1, rateDataDto_2, rateDataDto_3));
        when(linearRegressionServiceMock.calculateAverageRatelinearRegression(Arrays.asList(rateDataDto_1, rateDataDto_2, rateDataDto_3)))
                .thenReturn(Arrays.asList(rateDataDto_1, rateDataDto_2, rateDataDto_3, rateDataDto_1, rateDataDto_3));


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", "USD");
        params.add("startDate", "2018-03-28");
        params.add("stopDate", "2018-03-30");

        mockMvc.perform(get("/api/average_rates?code=USD&startDate=2018-03-28&stopDate=2018-03-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(5)))

                .andExpect(jsonPath("$[0].tableDate").value("2018-03-28"))
                .andExpect(jsonPath("$[0].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].mid").value("3.4145"))

                .andExpect(jsonPath("$[1].tableDate").value("2018-03-29"))
                .andExpect(jsonPath("$[1].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[1].code").value("USD"))
                .andExpect(jsonPath("$[1].mid").value("3.3956"))

                .andExpect(jsonPath("$[2].tableDate").value("2018-03-30"))
                .andExpect(jsonPath("$[2].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[2].code").value("USD"))
                .andExpect(jsonPath("$[2].mid").value("3.4139"));

        verify(averangeRatesRepositoryMock, times(1))
                .getRateDataByCodeAndTableDateBetween("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30"));

    }

    @Test
    public void getDataAverageRatesByCodeAndDataRangeIncorrectTest() throws Exception {

        when(averangeRatesRepositoryMock.getRateDataByCodeAndTableDateBetween
                ("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30")))
                .thenReturn(Arrays.asList(new RateDataDto()));


        mockMvc.perform(get("/api/average_rates?code=USD&startDate=2018-03-28&stopDate=2018-03-30"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/average_rates?code=USDq&startDate=2018-03-28&stopDate=2018-03-30"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("javax.validation.ConstraintViolationException"))
                .andExpect(jsonPath("errMsg").value("getAverageRatesByCodeAndDataRange.code: size must be between 3 and 3"));

        mockMvc.perform(get("/api/average_rates?code=USD&startDate=2018-03-&stopDate=2018-03-30"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("javax.validation.ConstraintViolationException"))
                .andExpect(jsonPath("errMsg").value("getAverageRatesByCodeAndDataRange.startDate: Wrong date format"));

        mockMvc.perform(get("/api/average_rates?code=USD&startDate=2018-03-28"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("org.springframework.web.bind.MissingServletRequestParameterException"))
                .andExpect(jsonPath("errMsg").value("Required String parameter 'stopDate' is not present"));

        verify(averangeRatesRepositoryMock, times(1))
                .getRateDataByCodeAndTableDateBetween("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30"));


    }

    @Test
    public void getDataTradingRatesAllCorrectTest() throws Exception {
        TradingRateDataDto tradingRateDataDto_1 = TradingRateDataDto.builder()
                .tableDate(Date.valueOf("2018-04-04"))
                .currency("dolar amerykański")
                .code("USD")
                .bid("3.3798")
                .ask("3.448")
                .build();
        TradingRateDataDto tradingRateDataDto_2 = TradingRateDataDto.builder()
                .tableDate(Date.valueOf("2018-04-04"))
                .currency("dolar australijski")
                .code("AUD")
                .bid("2.6025")
                .ask("2.6551")
                .build();


        when(buyAndSellRateRepositoryMock.getAllTraidingRateByDate(Date.valueOf("2018-04-04")))
                .thenReturn(Arrays.asList(tradingRateDataDto_1, tradingRateDataDto_2));

        mockMvc.perform(get("/api/current/trading_rates/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].tableDate").value("2018-04-04"))
                .andExpect(jsonPath("$[0].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].bid").value("3.3798"))
                .andExpect(jsonPath("$[0].ask").value("3.448"))

                .andExpect(jsonPath("$[1].tableDate").value("2018-04-04"))
                .andExpect(jsonPath("$[1].currency").value("dolar australijski"))
                .andExpect(jsonPath("$[1].code").value("AUD"))
                .andExpect(jsonPath("$[1].bid").value("2.6025"))
                .andExpect(jsonPath("$[1].ask").value("2.6551"));

        verify(buyAndSellRateRepositoryMock, times(1)).getAllTraidingRateByDate(Date.valueOf("2018-04-04"));
    }

    @Test
    public void getDataTraidingRatesByCodeAndDataRangeCorrectTest() throws Exception {


        TradingRateDataDto tradingRateDataDto_1 = TradingRateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-28"))
                .currency("dolar amerykański")
                .code("USD")
                .bid("3.367")
                .ask("3.435")
                .build();
        TradingRateDataDto tradingRateDataDto_2 = TradingRateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-29"))
                .currency("dolar amerykański")
                .code("USD")
                .bid("3.3821")
                .ask("3.4505")
                .build();
        TradingRateDataDto tradingRateDataDto_3 = TradingRateDataDto.builder()
                .tableDate(Date.valueOf("2018-03-30"))
                .currency("dolar amerykański")
                .code("USD")
                .bid("3.3821")
                .ask("3.4505")
                .build();

        when(buyAndSellRateRepositoryMock.getTradingRatesDataByCodeAndTableDateBetween
                ("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30")))
                .thenReturn(Arrays.asList(tradingRateDataDto_1, tradingRateDataDto_2, tradingRateDataDto_3));
        when(linearRegressionServiceMock.calculateTradingRateLinearRegression(Arrays.asList(tradingRateDataDto_1, tradingRateDataDto_2, tradingRateDataDto_3)))
                .thenReturn(Arrays.asList(tradingRateDataDto_1, tradingRateDataDto_2, tradingRateDataDto_3, tradingRateDataDto_1));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", "USD");
        params.add("startDate", "2018-03-28");
        params.add("stopDate", "2018-03-30");

        mockMvc.perform(get("/api/trading_rates").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)))

                .andExpect(jsonPath("$[0].tableDate").value("2018-03-28"))
                .andExpect(jsonPath("$[0].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].bid").value("3.367"))
                .andExpect(jsonPath("$[0].ask").value("3.435"))

                .andExpect(jsonPath("$[1].tableDate").value("2018-03-29"))
                .andExpect(jsonPath("$[1].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[1].code").value("USD"))
                .andExpect(jsonPath("$[1].bid").value("3.3821"))
                .andExpect(jsonPath("$[1].ask").value("3.4505"))

                .andExpect(jsonPath("$[2].tableDate").value("2018-03-30"))
                .andExpect(jsonPath("$[2].currency").value("dolar amerykański"))
                .andExpect(jsonPath("$[2].code").value("USD"))
                .andExpect(jsonPath("$[2].bid").value("3.3821"))
                .andExpect(jsonPath("$[2].ask").value("3.4505"));

        verify(buyAndSellRateRepositoryMock, times(1))
                .getTradingRatesDataByCodeAndTableDateBetween("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30"));
    }

    @Test
    public void getDataTraidingRatesByCodeAndDataRangeIncorrectTest() throws Exception {

        when(buyAndSellRateRepositoryMock.getTradingRatesDataByCodeAndTableDateBetween
                ("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30")))
                .thenReturn(Arrays.asList(new TradingRateDataDto()));


        mockMvc.perform(get("/api/trading_rates?code=USD&startDate=2018-03-28&stopDate=2018-03-30"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/trading_rates?code=USDq&startDate=2018-03-28&stopDate=2018-03-30"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("javax.validation.ConstraintViolationException"))
                .andExpect(jsonPath("errMsg").value("getDataTraidingRatesByCodeAndDataRange.code: size must be between 3 and 3"));

        mockMvc.perform(get("/api/trading_rates?code=USD&startDate=2018-03-&stopDate=2018-03-30"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("javax.validation.ConstraintViolationException"))
                .andExpect(jsonPath("errMsg").value("getDataTraidingRatesByCodeAndDataRange.startDate: Wrong date format"));

        mockMvc.perform(get("/api/trading_rates?code=USD&startDate=2018-03-28"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errCode").value("400"))
                .andExpect(jsonPath("errClassName").value("org.springframework.web.bind.MissingServletRequestParameterException"))
                .andExpect(jsonPath("errMsg").value("Required String parameter 'stopDate' is not present"));

        verify(buyAndSellRateRepositoryMock, times(1))
                .getTradingRatesDataByCodeAndTableDateBetween("USD", Date.valueOf("2018-03-28"), Date.valueOf("2018-03-30"));


    }


}
