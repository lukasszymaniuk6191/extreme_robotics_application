package com.lukas.er.monitoring.service;

import com.lukas.er.monitoring.dto.DataOfTheDownloadedFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDownloaderServiceImpl implements FileDownloaderService {

    private DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto;

    @Autowired
    public FileDownloaderServiceImpl(DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto) {
        this.dataOfTheDownloadedFileDto = dataOfTheDownloadedFileDto;
    }


    public boolean getPingStatus(String strURL) {
        boolean urlExist = false;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(strURL);
            connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                urlExist = true;
            }
        } catch (IOException e) {
            urlExist = false;
        }

        return urlExist;
    }

    public List<String> download() throws IOException {

        List<String> statusList = new ArrayList<>();
        List<DataOfTheDownloadedFileDto> dataOfTheDownloadedFileDtoList = dataOfTheDownloadedFileDto.getDataDownloadedFiles();

        for (int i = 0; i < dataOfTheDownloadedFileDtoList.size(); i++) {
            if (this.getPingStatus(dataOfTheDownloadedFileDtoList.get(i).getUrl())) {
                URL url = new URL(dataOfTheDownloadedFileDtoList.get(i).getUrl());
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                FileOutputStream fis = new FileOutputStream(dataOfTheDownloadedFileDtoList.get(i).getFile());
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = bis.read(buffer, 0, 1024)) != -1) {
                    fis.write(buffer, 0, count);
                }
                fis.close();
                bis.close();

                statusList.add("Correct url");
            } else {
                statusList.add("Incorrect url");
            }
        }

        return statusList;
    }

}
