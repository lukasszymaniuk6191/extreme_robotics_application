package com.lukas.er.monitoring.dto;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Component
public class DataOfTheDownloadedFileDto {

    private String file;
    private String url;
    @Autowired
    private DataOfTheDownloadedFilePropertiesDto dataPropertiesDto;

    public DataOfTheDownloadedFileDto(String filePath, String fileUrl) {
        this.file = filePath;
        this.url = fileUrl;
    }

    public List<DataOfTheDownloadedFileDto> getDataDownloadedFiles() {
        List<DataOfTheDownloadedFileDto> dataOfTheDownloadedFileDtoList = new ArrayList<>();
        String[] tableNames = dataPropertiesDto.getTableNames();

        LocalDate localDate = LocalDate.now();
        String date = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);


        for (int i = 0; i < tableNames.length; i++) {
            url = this.dataPropertiesDto.getTableBaseUrl() +
                    tableNames[i];
            file = dataPropertiesDto.getFilePath() + tableNames[i] +
                    "_" + date + dataPropertiesDto.getFileType();
            dataOfTheDownloadedFileDtoList.add(
                    new DataOfTheDownloadedFileDto(file, url));
        }


        return dataOfTheDownloadedFileDtoList;
    }


    public List<DataOfTheDownloadedFileDto> getDataDownloadedFilesFromTheLastMonth() {
        List<DataOfTheDownloadedFileDto> dataOfTheDownloadedFileDtoList = new ArrayList<>();
        String[] tableNames = dataPropertiesDto.getTableNames();

        LocalDate localDate = LocalDate.now();
        int lastNumberOfDays = 10;
        for (int i = lastNumberOfDays; i >= 0; i--) {
            String startDate = DateTimeFormatter.ofPattern("yyy-MM-dd")
                    .format(localDate.minusDays(i));
            for (int j = 0; j < tableNames.length; j++) {
                url = this.dataPropertiesDto.getTableBaseUrl() +
                        tableNames[j] + "/" + startDate;
                file = dataPropertiesDto.getFilePath() + tableNames[j] +
                        "_" + startDate + dataPropertiesDto.getFileType();
                dataOfTheDownloadedFileDtoList.add(
                        new DataOfTheDownloadedFileDto(file, url));
            }
        }

        return dataOfTheDownloadedFileDtoList;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataOfTheDownloadedFilePropertiesDto getDataPropertiesDto() {
        return dataPropertiesDto;
    }


    public void setDataPropertiesDto(DataOfTheDownloadedFilePropertiesDto dataPropertiesDto) {
        this.dataPropertiesDto = dataPropertiesDto;
    }

    @Override
    public String toString() {
        return "DataOfTheDownloadedFileDto{" +
                "file='" + file + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
