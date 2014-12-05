package com.pcube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartException;

import com.pcube.DBFile;
import com.pcube.FileUploadThread;



@Controller
public class ExperimentController {
	
	
    
    private static final Logger logger = LoggerFactory.getLogger(ExperimentController.class);
    

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file) throws MultipartException {
		DBFile currDBFile = null;

		if (!file.isEmpty()) {
			try {
				logger.info("handleFileUpload: start uploading SQLite db file from client to server");
							
				currDBFile = new DBFile(file);
				FileUploadThread th = new FileUploadThread(currDBFile, logger);
				th.start();
				logger.info("handleFileUpload: end uploading SQLite db file");
//				logger.info("handleFileUpload: start transfering data from SQLite db to MySQL db");
//                currDBFile.transferData();
//				logger.info("handleFileUpload: end transfering data from SQLite db to MySQL db");
//                currDBFile.delete();
				return "You successfully uploaded " + file.getOriginalFilename() + " !";
			} catch (Exception e) {
				logger.info(e.getMessage());
				return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
				
			}
		} else {
			return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
		}
	}
}
