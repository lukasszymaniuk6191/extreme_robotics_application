package com.lukas.er.monitoring.service;

import java.io.IOException;
import java.util.List;

public interface FileDownloaderService {

    boolean getPingStatus(String strURL);

    List<String> download() throws IOException;
}
