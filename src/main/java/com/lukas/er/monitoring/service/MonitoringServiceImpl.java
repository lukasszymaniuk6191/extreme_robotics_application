package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.dto.LoggerDto;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class MonitoringServiceImpl implements MonitoringService {


    private FileWatcherService fileWatcherService;
    private FileDownloaderService fileDownloaderService;
    private TableDataBaseService fileDbWriterService;

    private LoggerDto loggerDto = new LoggerDto(this.getClass());

    @Autowired
    public MonitoringServiceImpl(FileWatcherService fileWatcherService,
                                 FileDownloaderService fileDownloaderService,
                                 TableDataBaseService fileDbWriterService) throws IOException, InterruptedException {

        this.fileWatcherService = fileWatcherService;
        this.fileDownloaderService = fileDownloaderService;
        this.fileDbWriterService = fileDbWriterService;


    }

    @Scheduled(cron = "*/5 * * * * *")
    public void monitoringChangesInFolder() throws InterruptedException, IOException, ParseException {

        List<FileWatcherDataDto> fileWatcherDataDtoList = fileWatcherService.detectChangesInFolder();
        fileDbWriterService.updateDataInDb(fileWatcherDataDtoList);
    }

    @Scheduled(cron = "*/3600 * * * * *")
    public void downloadingTheFileFromUrl() throws IOException {

        fileDownloaderService.download();
        loggerDto.debug("Files downloaded");
    }

}
