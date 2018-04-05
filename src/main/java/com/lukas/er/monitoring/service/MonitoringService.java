package com.lukas.er.monitoring.service;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface MonitoringService {
    void monitoringChangesInFolder() throws InterruptedException, IOException, ParseException;
    void downloadingTheFileFromUrl() throws IOException;
}
