package edu.sjsu.cmpe275.lab1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	ApplicationContext fileService;
	IFileService fileServiceImpl;

	public App()
	{
		fileService=new ClassPathXmlApplicationContext("fileServiceBeans.xml");
		fileServiceImpl=(IFileService) fileService.getBean("fileServiceBean");
	}
	
	public void shareFile(String userId, String targetUserID, String filePath)
	{
		fileServiceImpl.shareFile(userId,targetUserID,filePath);		
	}
	
    public void unshareFile(String userId, String targetUserID, String filePath)
    {
	    fileServiceImpl.unshareFile(userId,targetUserID,filePath);
	}
	
    public void readFile(String userId, String filePath)
    {
	    fileServiceImpl.readFile(userId,filePath);
	}

}
