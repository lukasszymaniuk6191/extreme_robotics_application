package com.lukas.er;


import com.lukas.er.monitoring.dto.DataOfTheDownloadedFilePropertiesDto;
import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.entity.AverageRate;
import com.lukas.er.monitoring.entity.BuyAndSellRate;
import com.lukas.er.monitoring.repository.AverangeRatesRepository;
import com.lukas.er.monitoring.repository.BuyAndSellRateRepository;
import com.lukas.er.monitoring.service.JsonObjectReaderService;
import com.lukas.er.monitoring.service.TableDataBaseService;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableDataBaseServiceTest {


    @Mock
    private AverangeRatesRepository averangeRatesRepositoryMock;
    @Mock
    private BuyAndSellRateRepository buyAndSellRateRepositoryMock;
    @Mock
    private JsonObjectReaderService jsonObjectReaderServiceMock;
    @Autowired
    private TableDataBaseService tableDataBaseService;
    @Autowired
    DataOfTheDownloadedFilePropertiesDto dataOfTheDownloadedFilePropertiesDto;



    @Before
    public void setUp()
    {
        tableDataBaseService.setBuyAndSellRatesRepository(buyAndSellRateRepositoryMock);
        tableDataBaseService.setAverangeRatesRepository(averangeRatesRepositoryMock);
        tableDataBaseService.setJsonObjectReaderService(jsonObjectReaderServiceMock);
    }

    @Test
    public void tableDataBaseServiceCorrectTest() throws IOException, ParseException {

        when(jsonObjectReaderServiceMock.getAverageRate(dataOfTheDownloadedFilePropertiesDto.getFilePath(),"A_2018-03-28.json"))
                .thenReturn(new AverageRate());
        when(jsonObjectReaderServiceMock.getAverageRate(dataOfTheDownloadedFilePropertiesDto.getFilePath(),"B_2018-03-28.json"))
                .thenReturn(new AverageRate());
        when(jsonObjectReaderServiceMock.getBuyAndSellRate(dataOfTheDownloadedFilePropertiesDto.getFilePath(),"C_2018-03-28.json"))
                .thenReturn(new BuyAndSellRate());

        List<FileWatcherDataDto> inputFileWatcherDataDtoList = new ArrayList<>();
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("A_2018-03-28.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("B_2018-03-28.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-28.json","ENTRY_CREATE"));
        List<FileWatcherDataDto> outputFileWatcherDataDtoList
                = tableDataBaseService.updateDataInDb(inputFileWatcherDataDtoList);


        assertEquals(3,outputFileWatcherDataDtoList.size());
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("A_2018-03-28.json","ENTRY_CREATED")));
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("B_2018-03-28.json","ENTRY_CREATED")));
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("C_2018-03-28.json","ENTRY_CREATED")));


        when(jsonObjectReaderServiceMock.getBuyAndSellRate(dataOfTheDownloadedFilePropertiesDto.getFilePath(),"C_2018-03-30.json"))
                .thenReturn(new BuyAndSellRate());


        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-30.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-30.json","ENTRY_DELETE"));
        outputFileWatcherDataDtoList
                = tableDataBaseService.updateDataInDb(inputFileWatcherDataDtoList);

        assertEquals(5,outputFileWatcherDataDtoList.size());
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("C_2018-03-30.json","ENTRY_CREATED")));
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("C_2018-03-30.json","ENTRY_DELETED")));

    }

    @Test
    public void tableDataBaseServiceIncorrectTest() throws IOException, ParseException {

        List<FileWatcherDataDto> inputFileWatcherDataDtoList = new ArrayList<>();
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("_2018-03-28.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("B_2018-03-28.json","ENTRY_CREATES"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-28.json","ENTRY_C"));
        List<FileWatcherDataDto> outputFileWatcherDataDtoList
                = tableDataBaseService.updateDataInDb(inputFileWatcherDataDtoList);

        assertEquals(3,outputFileWatcherDataDtoList.size());
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("WRONG_FILENAME","FILE_NOT_ADDED")));
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("","WRONG_ENTRY")));
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("","WRONG_ENTRY")));



        inputFileWatcherDataDtoList=new ArrayList<>();
        outputFileWatcherDataDtoList=new ArrayList<>();
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-w0.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-.json","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-w0.jsox","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("x_2018-03-w0.jsox","ENTRY_CREATE"));
        inputFileWatcherDataDtoList.add(
                new FileWatcherDataDto("C_2018-03-w0.jsonC_2018-03-w0.json","ENTRY_CREATE"));
        outputFileWatcherDataDtoList
                = tableDataBaseService.updateDataInDb(inputFileWatcherDataDtoList);

        int numberDuplicatedObjects = Collections.frequency(outputFileWatcherDataDtoList,
                new FileWatcherDataDto("WRONG_FILENAME","FILE_NOT_ADDED"));

        assertEquals(inputFileWatcherDataDtoList.size(), outputFileWatcherDataDtoList.size());
        assertEquals(5, outputFileWatcherDataDtoList.size());
        assertEquals(5,numberDuplicatedObjects);
        assertTrue(outputFileWatcherDataDtoList.contains(
                new FileWatcherDataDto("WRONG_FILENAME","FILE_NOT_ADDED")));

    }
}


