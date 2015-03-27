package edu.sjsu.cmpe275.test.lab1;

import org.junit.Test;

import edu.sjsu.cmpe275.lab1.App;
import edu.sjsu.cmpe275.lab1.FileServiceImpl;
import edu.sjsu.cmpe275.lab1.IFileService;
import edu.sjsu.cmpe275.lab1.UnauthorizedException;

public class Validations {
	
	IFileService fileService=new FileServiceImpl();
	App app=new App();
	
	@Test (expected=UnauthorizedException.class) 
	  public void testA() {
	  System.out.println("Test Case A");
	  System.out.println("Bob trying to access Alice's File without it being shared. We get an Exception."); 
	  System.out.println("-------------------------------------------------------------------------------");
	  app.readFile("Bob","/home/Alice/shared/alicetext1.txt");
	  System.out.println("Test Completed with no exception!");
	  System.out.println("-------------------------------------------------------------------------------");
}
	  
	
	@Test()
	public void testB() {

		System.out.println("Test Case B");
		System.out.println("Bob trying to access Alice's File after it being shared. Bob can read this file.");
		System.out.println("--------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.readFile("Bob","/home/Alice/shared/alicetext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	@Test()
	public void testC() {

		System.out.println("Test Case C");
		System.out.println("Alice shares her file with Bob. Bob shares Alice's file with Carl. Both can read it");
		System.out.println("-----------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.readFile("Bob","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Bob","Carl","/home/Alice/shared/alicetext1.txt");
		app.readFile("Carl","/home/Alice/shared/alicetext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("-----------------------------------------------------------------------------------");
	}
	
	@Test (expected=UnauthorizedException.class) 
	public void testD() {

		System.out.println("Test Case D");
		System.out.println("Alice shares her file with Bob. Bob shares Carl's file with Alice. We get an Exception!");
		System.out.println("---------------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Bob","Alice","/home/Carl/shared/carltext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("---------------------------------------------------------------------------------------");
	}
	
	@Test (expected=UnauthorizedException.class) 
	public void testE() {

		System.out.println("Test Case E");
		System.out.println("Alice shares her file with Bob. Bob shares Alice's file with Carl. Alice unshares her file with Carl. Carl can't read this file!");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Bob","Carl","/home/Alice/shared/alicetext1.txt");
		app.unshareFile("Alice","Carl","/home/Alice/shared/alicetext1.txt");
		app.readFile("Carl","/home/Alice/shared/alicetext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
	}
	
	@Test (expected=UnauthorizedException.class) 
	public void testF() {

		System.out.println("Test Case F");
		System.out.println("Alice shares her file with Bob. Alice shared her file with Carl. Carl shares Alice's file with Bob. Alice unshares her file with Bob. Bob can't read this file!");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Alice","Carl","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Carl","Bob","/home/Alice/shared/alicetext1.txt");
		app.unshareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.readFile("Bob","/home/Alice/shared/alicetext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	@Test (expected=UnauthorizedException.class) 
	public void testG() {

		System.out.println("Test Case G");
		System.out.println("Alice shares her file with Bob. Bob shares Alice's file with Carl. Alice unshares her file with Bob. Bob shares Alice's file with Carl again. We get an exception. But Carl can still read this file.");
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Bob","Carl","/home/Alice/shared/alicetext1.txt");
		app.unshareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.readFile("Carl","/home/Alice/shared/alicetext1.txt");
		app.shareFile("Bob","Carl","/home/Alice/shared/alicetext1.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	@Test (expected=UnauthorizedException.class) 
	public void testH() {

		System.out.println("Test Case H");
		System.out.println("Bob trying to access Alice's File2 when only File1 is shared. We get an Exception!");
		System.out.println("----------------------------------------------------------------------------------");
		app.shareFile("Alice","Bob","/home/Alice/shared/alicetext1.txt");
		app.readFile("Bob","/home/Alice/shared/alicetext2.txt");
		System.out.println("Test Completed with no exception!");
		System.out.println("----------------------------------------------------------------------------------");
	}
	
}
