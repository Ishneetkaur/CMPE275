package edu.sjsu.cmpe275.lab1;
import java.util.HashMap;
import java.util.Set;

public class FileServiceImpl implements IFileService {
	
	private static HashMap<String,HashMap<String,Set<String>>> sharedFiles=new HashMap<String,HashMap<String,Set<String>>>();

	public void shareFile(String userId, String targetUserID, String filePath) {
		System.out.println(userId+" shared file "+ filePath+" with "+targetUserID);
	}

	public void unshareFile(String userId, String targetUserID, String filePath) {
		System.out.println(userId+" unshared file "+ filePath+" with "+targetUserID);
	}

	public byte[] readFile(String userId, String filePath) {
		System.out.println(userId+" reads the file "+ filePath);
		return null;
	}
	
	public static HashMap<String,HashMap<String,Set<String>>> getSharedFilesData()
	{  	return sharedFiles;
	}
	
	public  static String getFileNameFromFilepath(String path)
	{
		String[] seprated=path.split("/");
		String fileName=seprated[seprated.length-1];
		return fileName;
	}
	
	public  static String getFileOwnerName(String path)
	{
		String[] seprated=path.split("/");
		String fileName=seprated[2];
		return fileName;
	}
	
	public static void setSharedFiles(HashMap<String,HashMap<String,Set<String>>> updatedData)
	{
		sharedFiles = updatedData;
	}
}
