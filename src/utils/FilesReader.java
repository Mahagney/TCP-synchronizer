package utils;

import java.io.File;

public class FilesReader {

	public static void main (String args[]) {

		displayIt(new File("C:/Users/Mahag/workspace/TCP-synchronizer/src/resources"));
	}
	
	public static void displayIt(File node){
		
		System.out.println(node.getAbsoluteFile());
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				displayIt(new File(node, filename));
			}
		}
		
	}
}
