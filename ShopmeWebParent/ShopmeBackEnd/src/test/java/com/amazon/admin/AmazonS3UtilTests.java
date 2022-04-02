package com.amazon.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTests {

	@Test
	public void testListFolder() {
		String folderName = "product-images/10/";
		
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		listKeys.forEach(System.out::println);
	}
	@Test
	public void testUploadFile() throws FileNotFoundException {
		  String folderName="test-upload/one";
		  String fileName = "Arun_Award.pdf";
		  String path="C:\\Users\\arunk\\OneDrive\\Desktop\\"+fileName;
		  InputStream inputStream= new FileInputStream(path);
		  AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}
	@Test
	public void testDeleteFile() {
		String fileName="test-upload/one/Arun_Award.pdf";
		AmazonS3Util.deleteFile(fileName);
		
	}
	@Test
	public void testRemoveFolder() {
		String folderName="test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
}
