package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.DataOfTheDownloadedFileDto;
import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.entity.AverageRate;
import com.lukas.er.monitoring.entity.BuyAndSellRate;
import com.lukas.er.monitoring.repository.AverangeRatesRepository;
import com.lukas.er.monitoring.repository.BuyAndSellRateRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableDataBaseServiceImpl implements TableDataBaseService{

    private JsonObjectReaderService jsonObjectReaderService;
    private DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto;
    private AverangeRatesRepository averangeRatesRepository;
    private BuyAndSellRateRepository buyAndSellRatesRepository;

    @Autowired
    public TableDataBaseServiceImpl(JsonObjectReaderService jsonObjectReaderService,
                                    DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto,
                                    AverangeRatesRepository averangeRatesRepository,
                                    BuyAndSellRateRepository buyAndSellRatesRepository)
    {
        this.jsonObjectReaderService = jsonObjectReaderService;
        this.dataOfTheDownloadedFileDto = dataOfTheDownloadedFileDto;
        this.averangeRatesRepository = averangeRatesRepository;
        this.buyAndSellRatesRepository = buyAndSellRatesRepository;
    }

    public List<FileWatcherDataDto> updateDataInDb(List<FileWatcherDataDto> fileWatcherDataDtoList) throws ParseException, IOException {
        List<FileWatcherDataDto> fileWatcherList = new ArrayList<>();

        for(FileWatcherDataDto fileWatcherDataDto:fileWatcherDataDtoList)
        {
            fileWatcherList.add(this.detectChangesInFolder(fileWatcherDataDto));
        }

        return fileWatcherList;
    }

    public FileWatcherDataDto detectChangesInFolder(FileWatcherDataDto fileWatcherDataDto) throws ParseException, IOException {
        String event = "WRONG_ENTRY";
        String fileName = "";

        if ("ENTRY_CREATE".equals(fileWatcherDataDto.getEventName())) {
            event="FILE_NOT_ADDED";
            fileName = "WRONG_FILENAME";
            if(this.saveTable(fileWatcherDataDto.getFilename()))
            {
                fileName = fileWatcherDataDto.getFilename();
                event = fileWatcherDataDto.getEventName()+"D";
            }
        } else if ("ENTRY_MODIFY".equals(fileWatcherDataDto.getEventName())) {

        } else if ("ENTRY_DELETE".equals(fileWatcherDataDto.getEventName())) {
            event="FILE_NOT_ADDED";
            fileName = "WRONG_FILENAME";
            if(this.deleteTable(fileWatcherDataDto.getFilename()))
            {
                fileName = fileWatcherDataDto.getFilename();
                event = fileWatcherDataDto.getEventName()+"D";
            }
        }

        return new FileWatcherDataDto(fileName,event);
    }


    public boolean deleteTable(String fileName)
    {
        boolean fileDeleted = false;

        if(/*fileName.contains("C_")*/fileNameIsCorrect(fileName, "C"))
        {
            buyAndSellRatesRepository.deleteBuyAndSellRateByFileName(fileName);
            fileDeleted = true;
        } else if(/*fileName.contains("A_") || fileName.contains("B_")*/
                fileNameIsCorrect(fileName, "A") || fileNameIsCorrect(fileName, "B"))
        {
            averangeRatesRepository.deleteAverageRateByFileName(fileName);
            fileDeleted = true;
        }
        else{
            fileDeleted = false;
        }

        return fileDeleted;
    }

    public boolean fileNameIsCorrect(String fileName, String tabName)
    {

        boolean tableNameIsCorrect = false;
        boolean dateIsCorrect = false;
        boolean fileTypeIsCorrect = false;

        if(fileName.length()!=17)
        {
            return false;
        }

        String[] str1 = fileName.split("_");
        String tableName = str1[0];
        if(tableName.equals(tabName))
        {
            tableNameIsCorrect = true;
        }

        String[] str2 = str1[1].split("\\.");
        String date = str2[0];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
            dateIsCorrect = true;
        } catch (java.text.ParseException e) {
           dateIsCorrect = false;
        }

        String fileType = str2[1];
        if(fileType.equals("json"))
        {
            fileTypeIsCorrect = true;
        }


        return ((tableNameIsCorrect==true) && (dateIsCorrect==true) && (fileTypeIsCorrect==true));


    }

    public boolean saveTable(String fileName) throws IOException, ParseException {

        boolean fileSaved = false;
        LocalDate localDate = LocalDate.now();
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);


        if(fileNameIsCorrect(fileName, "C"))
        {
            BuyAndSellRate buyAndSellRates = jsonObjectReaderService.getBuyAndSellRate(dataOfTheDownloadedFileDto.getDataPropertiesDto().getFilePath(),fileName);
            buyAndSellRates.setTableDate(Date.valueOf(date));
            buyAndSellRatesRepository.save(buyAndSellRates);
            fileSaved = true;
        } else if(fileNameIsCorrect(fileName, "A") || fileNameIsCorrect(fileName, "B"))
        {
            AverageRate averageRate = jsonObjectReaderService.getAverageRate(dataOfTheDownloadedFileDto.getDataPropertiesDto().getFilePath(),fileName);
            averageRate.setTableDate(Date.valueOf(date));
            averangeRatesRepository.save(averageRate);
            fileSaved = true;
        }
        else{

            fileSaved = false;
        }
        return fileSaved;
    }



    public void setJsonObjectReaderService(JsonObjectReaderService jsonObjectReaderService) {
        this.jsonObjectReaderService = jsonObjectReaderService;
    }

    public void setAverangeRatesRepository(AverangeRatesRepository averangeRatesRepository) {
        this.averangeRatesRepository = averangeRatesRepository;
    }

    public void setBuyAndSellRatesRepository(BuyAndSellRateRepository buyAndSellRatesRepository) {
        this.buyAndSellRatesRepository = buyAndSellRatesRepository;
    }
}
