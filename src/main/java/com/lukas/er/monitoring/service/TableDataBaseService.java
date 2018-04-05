package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.repository.AverangeRatesRepository;
import com.lukas.er.monitoring.repository.BuyAndSellRateRepository;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface TableDataBaseService {
    List<FileWatcherDataDto> updateDataInDb(List<FileWatcherDataDto> fileWatcherDataDtoList) throws ParseException, IOException;

    FileWatcherDataDto detectChangesInFolder(FileWatcherDataDto fileWatcherDataDto) throws ParseException, IOException;

    boolean deleteTable(String fileName);

    boolean saveTable(String filename) throws IOException, ParseException;

    void setBuyAndSellRatesRepository(BuyAndSellRateRepository buyAndSellRatesRepository);

    void setAverangeRatesRepository(AverangeRatesRepository averangeRatesRepository);

    void setJsonObjectReaderService(JsonObjectReaderService jsonObjectReaderService);
}