package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FilesReader {
	private String path;
	
	public FilesReader(String path){
		this.path=path;
	}
	
	public static void main (String args[]) {

		FilesReader fr=new FilesReader("C:\\Users\\Mahag\\workspace\\TCP-synchronizer\\src\\resources");
		fr.getFilesList();
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
	public Map<String,Long> getFilesList(){
		return getFilesList(new File(path));
	}
	
	public Map<String,Long> getFilesList(File node){
		Map<String,Long> files=new HashMap<String,Long>();
		String fp=node.getAbsolutePath();
		System.out.println(fp);
		System.out.println(path);
		int i=fp.indexOf(path);
		String relativePath=fp.substring(path.length());
		System.out.println("  "+relativePath);
		files.put(relativePath,node.length());
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				getFilesList(new File(node, filename));
			}
		}

		return files;
	}
}
