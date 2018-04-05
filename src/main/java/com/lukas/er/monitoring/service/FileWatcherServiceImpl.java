package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.DataOfTheDownloadedFileDto;
import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.dto.LoggerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class FileWatcherServiceImpl implements  FileWatcherService{

    private LoggerDto loggerDto = new LoggerDto(this.getClass());
    private WatchService watcher;
    private Path path;
    private DataOfTheDownloadedFileDto fileData;

    @Autowired
    public FileWatcherServiceImpl(DataOfTheDownloadedFileDto fileData) throws IOException {
        this.fileData = fileData;

        this.watcher = FileSystems.getDefault().newWatchService();
        this.path = Paths.get(fileData.getDataPropertiesDto().getFilePath());
        this.path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
    }

    public void setFileWatcherParams(WatchService watcher, Path logDir)
    {
        this.watcher = watcher;
        this.path = logDir;
    }

    public List<FileWatcherDataDto> detectChangesInFolder() throws InterruptedException {

        WatchKey key = watcher.take();
        List<FileWatcherDataDto> fileWatcherDataDtoList = new ArrayList<>();

        final BiFunction<WatchEvent<?>, WatchEvent.Kind<?>, FileWatcherDataDto> watchEventKindObjectBiFunction = (WatchEvent<?> events, WatchEvent.Kind<?> kind) -> {
            return new FileWatcherDataDto(events.context().toString(), kind.name().toString());
        };

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();

            if (ENTRY_CREATE.equals(kind)) {
                System.out.println("Entry was created on log dir.");
                loggerDto.debug("Entry was created on log dir.");
                fileWatcherDataDtoList.add(watchEventKindObjectBiFunction.apply(event,kind));
            } else if (ENTRY_MODIFY.equals(kind)) {
                System.out.println("Entry was modified on log dir.");
                loggerDto.debug("Entry was modified on log dir.");
                fileWatcherDataDtoList.add(watchEventKindObjectBiFunction.apply(event,kind));
            } else if (ENTRY_DELETE.equals(kind)) {
                System.out.println("Entry was deleted from log dir.");
                loggerDto.debug("Entry was deleted from log dir.");
                fileWatcherDataDtoList.add(watchEventKindObjectBiFunction.apply(event,kind));
            }
        }
        key.reset();

        return  fileWatcherDataDtoList;
    }



}
