package com.lukas.er;

import com.lukas.er.monitoring.dto.DataOfTheDownloadedFileDto;
import com.lukas.er.monitoring.dto.FileWatcherDataDto;
import com.lukas.er.monitoring.service.FileWatcherService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FileWatcherServiceTest {

    @Autowired
    private FileWatcherService fileWatcherService;

    @Autowired
    private DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void init() {
        dataOfTheDownloadedFileDto.getDataPropertiesDto().setFilePath(folder.getRoot().getPath());
    }

    @Test
    public void fileWatcherServiceCorrectTest() throws IOException, InterruptedException {


        List<FileWatcherDataDto> fileWatcherDataDtoList = new ArrayList<>();
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(folder.getRoot().getPath());
        path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        fileWatcherService.setFileWatcherParams(watcher, path);

        folder.newFile("file1");
        folder.newFile("file2");
        folder.newFile("file3");

        fileWatcherDataDtoList = fileWatcherService.detectChangesInFolder();

        assertEquals(3, fileWatcherDataDtoList.size());
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file1", "ENTRY_CREATE")));
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file2", "ENTRY_CREATE")));
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file3", "ENTRY_CREATE")));
        assertFalse(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file7", "ENTRY_CREATE")));
        assertFalse(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file1", "ENTRY")));


        folder.delete();
        fileWatcherDataDtoList = null;
        fileWatcherDataDtoList = fileWatcherService.detectChangesInFolder();

        assertEquals(3, fileWatcherDataDtoList.size());
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file1", "ENTRY_DELETE")));
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file2", "ENTRY_DELETE")));
        assertTrue(fileWatcherDataDtoList.contains(new FileWatcherDataDto("file3", "ENTRY_DELETE")));


    }


}
