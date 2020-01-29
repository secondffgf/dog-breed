package com.majoapps.dogbreed.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

@RunWith(MockitoJUnitRunner.class)
public class AWSServiceTest {
	private static final String TEST_LOCATION = "http://testLocation.com";

	private static final String TEST_FILE = "testFile";

	@Mock
	private AmazonS3 s3client;
	
	@InjectMocks
	private AWSService awsService;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("add file to s3")
	public void addFile() throws MalformedURLException {
		when(s3client.putObject(null, TEST_FILE, TEST_LOCATION))
			.thenReturn(new PutObjectResult());
		when(s3client.getUrl(null, TEST_FILE)).thenReturn(new URL(TEST_LOCATION));
		URL location = awsService.addFile(TEST_FILE, TEST_LOCATION);
		assertEquals(TEST_LOCATION, location.toString());
	}
	
	@Test
	@DisplayName("add file to s3 with exception")
	public void addFileThrowsException() {
		when(s3client.putObject(null, TEST_FILE, TEST_LOCATION))
			.thenThrow(new AmazonClientException("Wrong location"));
		assertThrows(RuntimeException.class, () -> {
			awsService.addFile(TEST_FILE, TEST_LOCATION);
		});
	}
	
	@Test
	@DisplayName("delete file from s3")
	public void deleteFile() {
		doNothing().when(s3client).deleteObject(null, TEST_FILE);
		awsService.deleteFile(TEST_FILE);
	}
	
	@Test
	@DisplayName("delete file from s3 with exception")
	public void deleteFileThrowsException() {
		doThrow(new AmazonClientException("Wrong file name")).when(s3client).deleteObject(null, TEST_FILE);
		assertThrows(RuntimeException.class, () -> {
			awsService.deleteFile(TEST_FILE);
		});
	}
}
