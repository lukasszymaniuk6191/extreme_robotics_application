package com.lukas.er.monitoring.dto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoggerDto {

    private Logger logger;
    private String monitoringDate;
    private LocalDate localDate;


    public LoggerDto(Class<?> cls) {
        logger = LoggerFactory.getLogger(cls);
    }

    public void debug(String debug) {
        monitoringDate = getCurrentDate();
        logger.debug(monitoringDate + debug);
    }

    public String getCurrentDate() {
        localDate = LocalDate.now();
        return "[Monitoring   " + DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate) + "]    ";
    }


}
