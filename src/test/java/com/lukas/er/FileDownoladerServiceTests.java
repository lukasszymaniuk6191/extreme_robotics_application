package com.lukas.er;

import com.lukas.er.monitoring.dto.DataOfTheDownloadedFileDto;
import com.lukas.er.monitoring.dto.DataOfTheDownloadedFilePropertiesDto;
import com.lukas.er.monitoring.service.FileDownloaderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDownoladerServiceTests {

	@Autowired
	private DataOfTheDownloadedFileDto dataOfTheDownloadedFileDto;
	@Autowired
	private DataOfTheDownloadedFilePropertiesDto dataPropertiesDto;
	@Autowired
	private FileDownloaderService fileDownloaderService;

	@Test
	public void fileDownloaderServiceCorrectTest() throws IOException {

		dataPropertiesDto.setTableNames(new String[]{"A", "B", "C"});
		dataPropertiesDto.setTableBaseUrl("http://api.nbp.pl/api/exchangerates/tables/");

		boolean urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(0).getUrl());
		assertTrue(urlExist);
		urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(1).getUrl());
		assertTrue(urlExist);
		urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(2).getUrl());
		assertTrue(urlExist);


		List<String> statusList1 = fileDownloaderService.download();
		List<String> statusList2 = new ArrayList<>();
		statusList2.add(new String("Correct url"));
		statusList2.add(new String("Correct url"));
		statusList2.add(new String("Correct url"));

		assertEquals(3,statusList1.size());
		assertTrue(equalLists(statusList1,statusList2));

	}



	@Test
	public void fileDownloaderServiceInCorrectTest() throws IOException {

		dataPropertiesDto.setTableBaseUrl("http://api.nbp.pl/api/exchangerates/tables/");
		dataPropertiesDto.setTableNames(new String[]{"AZ", "B", "CZ"});

		boolean urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(0).getUrl());
		assertFalse(urlExist);
		urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(1).getUrl());
		assertTrue(urlExist);
		urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(2).getUrl());

		assertFalse(urlExist);


		List<String> statusList1 = fileDownloaderService.download();
		List<String> statusList2 = new ArrayList<>();
		statusList2.add(new String("Incorrect url"));
		statusList2.add(new String("Correct url"));
		statusList2.add(new String("Incorrect url"));

		assertTrue(equalLists(statusList1,statusList2));


		dataPropertiesDto.setTableNames(new String[]{"AZ", "B", "C"});
		statusList1 = fileDownloaderService.download();
		statusList2= new ArrayList<>();
		statusList2.add(new String("Correct url"));
		statusList2.add(new String("Correct url"));
		statusList2.add(new String("Incorrect url"));

		assertTrue(equalLists(statusList1,statusList2));


		dataPropertiesDto.setTableBaseUrl("XXX");
		urlExist = fileDownloaderService.getPingStatus(dataOfTheDownloadedFileDto.getDataDownloadedFiles().get(0).getUrl());
		assertFalse(urlExist);

		statusList1 = fileDownloaderService.download();
		statusList2= new ArrayList<>();
		statusList2.add(new String("Incorrect url"));
		statusList2.add(new String("Incorrect url"));
		statusList2.add(new String("Incorrect url"));

		assertTrue(equalLists(statusList1,statusList2));


	}



	public  boolean equalLists(List<String> one, List<String> two){
		if (one == null && two == null){
			return true;
		}

		if((one == null && two != null)
				|| one != null && two == null
				|| one.size() != two.size()){
			return false;
		}

		one = new ArrayList<String>(one);
		two = new ArrayList<String>(two);

		Collections.sort(one);
		Collections.sort(two);
		return one.equals(two);
	}

}
