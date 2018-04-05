package com.lukas.er.monitoring.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class DataOfTheDownloadedFilePropertiesDto {

    @Value("${extreme.robotics.tableBaseUrl}")
    private String tableBaseUrl;
    @Value("${extreme.robotics.tableNames}")
    private String[] tableNames;
    @Value("${extreme.robotics.filePath}")
    public String filePath;
    @Value("${extreme.robotics.fileType}")
    private String fileType;


}
