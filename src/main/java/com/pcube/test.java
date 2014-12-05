package com.pcube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.pcube.DBFile;
import com.pcube.FileUploadThread;

public class test {

	private static final Logger logger = LoggerFactory.getLogger(test.class);
		
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		DBFile currDBFile = null;
		
		currDBFile = new DBFile(new File("C:/Users/Joonwon/Documents/workspace-sts-3.6.2.RELEASE/Server_pcube/2014_12_04_23_29_12_db"));
		FileUploadThread th = new FileUploadThread(currDBFile, logger);
		th.start();
		
	}


}
