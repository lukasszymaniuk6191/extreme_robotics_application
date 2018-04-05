package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.FileWatcherDataDto;

import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.List;

public interface FileWatcherService {
    void setFileWatcherParams(WatchService watcher, Path logDir);
    List<FileWatcherDataDto> detectChangesInFolder() throws InterruptedException;

}
