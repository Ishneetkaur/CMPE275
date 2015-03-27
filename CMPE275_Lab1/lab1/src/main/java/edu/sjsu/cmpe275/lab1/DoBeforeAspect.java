package edu.sjsu.cmpe275.lab1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class DoBeforeAspect {
	
	@Before("execution(* edu.sjsu.cmpe275.lab1.IFileService.*(..))")
	public void doBefore(JoinPoint joinPoint) {

		 String methodCalled = joinPoint.getSignature().getName();
			
			System.out.println("<<<<<AspectJ>>>>> DoBefore() is running. It intercepted : "+ methodCalled);
			
			if(methodCalled.equals("readFile"))
			{   
				readFileOperation(joinPoint);
			}
			else if(methodCalled.equals("shareFile"))
			{   
				shareFileOperation(joinPoint);
			}
			else if(methodCalled.equals("unshareFile"))
			{
				unshareFileOperation(joinPoint);
			}
	       
	}
	 
	public void readFileOperation(JoinPoint joinPoint)
	{     Object[] parameters=(Object[])joinPoint.getArgs();
		  String fileOwner = FileServiceImpl.getFileOwnerName(parameters[parameters.length-1].toString());
		  String fileName = FileServiceImpl.getFileNameFromFilepath(parameters[parameters.length-1].toString());
		  String userId=parameters[0].toString();
		  System.out.println("<< " +userId + " is trying to access " + fileName + " >>");
		  HashMap<String,HashMap<String,Set<String>>> sharedFiles=FileServiceImpl.getSharedFilesData();
						
		  if(sharedFiles.containsKey(fileOwner))
			{
				HashMap<String,Set<String>> hm=(HashMap<String,Set<String>> )sharedFiles.get(fileOwner);
				if(hm.containsKey(fileName))
					
				{   
					Set<String> list=hm.get(fileName);
								
					if(list.contains(userId))
				    { 
					  System.out.println("Valid User :" + userId + " to read " + fileName);
				    }
				  
				    else
				    {
				    	System.out.println("Throw Exception : Not Authorized for " + userId + " to read " + fileName);
				    	throw new UnauthorizedException("Throw Exception : Not Authorized!!");
				    }
			    }
				else
			    {
			    	System.out.println("Throw Exception :" + fileName + " Not Shared by " + fileOwner);
			    	throw new UnauthorizedException("Throw Exception : File Not Shared!!");
			    }
				
			}
			else
			{
				System.out.println("Throw Exception :" + fileOwner + " hasn't shared " + fileName);
				throw new UnauthorizedException(fileOwner + " hasn't shared " + fileName);
			
			}
			
	 }
	 
	 public void shareFileOperation(JoinPoint joinPoint)
	 {
		 
			Object[] parameters=(Object[])joinPoint.getArgs();
			
			String fileName= FileServiceImpl.getFileNameFromFilepath(parameters[parameters.length-1].toString());
			String userId=parameters[0].toString();
			String targetUserID=parameters[1].toString();
			HashMap<String,HashMap<String,Set<String>>> sharedFiles=FileServiceImpl.getSharedFilesData();
			String fileOwner = FileServiceImpl.getFileOwnerName(parameters[parameters.length-1].toString());
			System.out.println("<< " +userId + " is trying to share " + fileName + " with " + targetUserID + " >>");
			if(!userId.equals(targetUserID) && (userId.equals(fileOwner) || hasRights(userId,fileOwner,fileName,sharedFiles) )){
			if(sharedFiles.containsKey(fileOwner))
			{
				HashMap<String,Set<String>> hm=(HashMap<String,Set<String>> )sharedFiles.get(fileOwner);
				if(hm.containsKey(fileName))
				{
					
					hm.get(fileName).add(targetUserID);
				   
				}
				else
				{
					Set<String> results=new HashSet<String>();
					results.add(targetUserID);
					hm.put(fileName, results);
				}
			}
			else
			{
				HashMap<String,Set<String>> hm=new HashMap<String,Set<String>>();
				Set<String> results=new HashSet<String>();
				results.add(targetUserID);
				hm.put(fileName, results);
				sharedFiles.put(fileOwner, hm);
			}
			
			}
			else
			{
				if(!hasRights(userId,fileOwner,fileName,sharedFiles))
				{
					System.out.println("Throw Exception : Not authorised for " + userId + " to share " + fileName + " with " + targetUserID);
					throw new UnauthorizedException("Throw Exception : Not authorised for " + userId + " to share " + fileName + " with " + targetUserID);
				}
				else
				{
					System.out.println(" No Action.");
				}
				
			}
	 }
	 
	 public void unshareFileOperation(JoinPoint joinPoint)
	 {
		 Object[] parameters=(Object[])joinPoint.getArgs();
			
			String fileOwner = FileServiceImpl.getFileOwnerName(parameters[parameters.length-1].toString());
			String fileName = FileServiceImpl.getFileNameFromFilepath(parameters[parameters.length-1].toString());
			String targetUserid=parameters[1].toString();
			String userId=parameters[0].toString();
			
			HashMap<String,HashMap<String,Set<String>>> sharedFiles=FileServiceImpl.getSharedFilesData();
			System.out.println("<< " +userId + " is trying to unshare " + fileName + " from " + targetUserid + " >>");
			if(userId.equals(fileOwner) && !userId.equals(targetUserid))
			{
			if(sharedFiles.containsKey(fileOwner))
			{
				HashMap<String,Set<String>> hm=(HashMap<String,Set<String>> )sharedFiles.get(fileOwner);
				if(hm.containsKey(fileName))
					
				{   
					Set<String> list=hm.get(fileName);
					
					
					if(list.contains(targetUserid))
				    {
					  sharedFiles.get(fileOwner).get(fileName).remove(targetUserid);
					  FileServiceImpl.setSharedFiles(sharedFiles);
					  list.remove(targetUserid);
				    }
				  
				    else
				    {
				    	System.out.println("Throw Exception - Target user:" + targetUserid + " does not exist in shared list.");
				    	throw new UnauthorizedException("Target user does not exist in shared list.");
				    }
			    }
				else
			    {
			    	System.out.println("Throw Exception : File - " + fileName + " Not Shared with " + targetUserid);
			    	throw new UnauthorizedException("Throw Exception : File Not Shared!!");
			    }
				
			}
			else
			{
				System.out.println("Throw Exception :" + userId + " hasn't shared " + fileName);
				throw new UnauthorizedException("User hasn't shared anything yet!!");
			
			}
			}
			else
			{
				System.out.println("Throw Exception :" +userId+" does not own "+ fileName);
				throw new UnauthorizedException(userId+" does not own "+ fileName);
			}
			
	 }
	 
	 public boolean hasRights(String userid,String fileOwner,String fileName,Object sharedFile)
	 {
		 HashMap<String,HashMap<String,Set<String>>> sharedFiles=FileServiceImpl.getSharedFilesData();
		 
		 HashMap<String,Set<String>> hm=(HashMap<String,Set<String>>)sharedFiles.get(fileOwner);
		 
		 try
		 {
		 Set<String> list=(Set<String>)hm.get(fileName);
		 
			if(list.contains(userid))
		    {
			    return true;
		    }
			else
			{
				return false;
			}
		 }
		 catch(Exception e)
		 {
			 return false;
		 }
			

	 }
}
