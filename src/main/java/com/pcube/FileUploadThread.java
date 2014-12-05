package com.pcube;

// Logger
import org.slf4j.Logger;

import com.pcube.DBFile;

public class FileUploadThread extends Thread {
	DBFile dbFile;
	Logger dbLogger;
	
	public FileUploadThread(DBFile newDBFile, Logger newLogger) {
		this.dbFile = newDBFile; 
		this.dbLogger = newLogger;
	}

	public void run() {
		try {
            this.dbLogger.info("handleFileUpload: start transfering data from SQLite db to MySQL db");
            this.dbFile.transferData();
            this.dbLogger.info("handleFileUpload: end transfering data from SQLite db to MySQL db");
            this.dbFile.delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}