package com.db.core;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.db.collection.reader.CollectionHelper;


public class main {

	public static long fullTime;
	public static long indexTime;
	
	public static void main(String[] args) {

//		System.out.println("Enter Folder Path");
//		Scanner scanner = new Scanner(System.in);
//		String folderPath = scanner.nextLine();
		
		
		CollectionHelper jsonHelper = new CollectionHelper("C:\\Users\\itsme\\Desktop\\Truchet Programming\\Inverted Indexing\\JsonFiles\\");//
		File[] listOfFiles = jsonHelper.getFilesList();
		
		
		Map<String, ArrayList<Map<String, String>>> JsonMapList = jsonHelper.getJsonMapList(listOfFiles);
		
		Map<String, Map<String, String>> IndexedMap =jsonHelper.seperateMapEntries(JsonMapList, listOfFiles);
		String searchField = "kitchen";
		
		Instant start = Instant.now();
		List<String> fileName = jsonHelper.searchIndex(IndexedMap, searchField);
		Instant finish = Instant.now();
		
		System.out.println( searchField + " word is present in file = " + fileName);
		
		fullTime = Duration.between(start, finish).toMillis();
		System.out.println("Time Consumed = " + (fullTime + indexTime));
		
		
		//jsonHelper.writeResultSet(listOfJson);
		
		
		
	}

}
