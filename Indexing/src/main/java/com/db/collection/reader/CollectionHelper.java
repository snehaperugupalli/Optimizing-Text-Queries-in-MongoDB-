package com.db.collection.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.db.collection.document.parser.DocumentParser;
import com.db.core.main;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CollectionHelper {

	DocumentParser documentParser = DocumentParser.getInstance();
	public static String FOLDER_PATH = "";
	public static String RESULT_FOLDER = "C:\\Users\\itsme\\Desktop\\Truchet Programming\\Inverted Indexing\\JsonFiles\\ResultSet\\";
	public Map<String, ArrayList<Map<String, String>>> JsonMapList = new LinkedHashMap<String, ArrayList<Map<String, String>>>();
	public static Map<String, Map<String, String>> IndexedMap= new HashMap<String, Map<String, String>>();
	// C:\\Users\\itsme\\Desktop\\Truchet Programming\\joins\\JsonFiles\\

	public CollectionHelper(String folder_path) {
		FOLDER_PATH = folder_path;
	}

	public static String readData(String fileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get(FOLDER_PATH + fileName)));
	}

	public File[] getFilesList() {
		File folder = new File(FOLDER_PATH);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}

	public Map<String, ArrayList<Map<String, String>>> getJsonMapList(File[] listOfFiles) {

		for (File file : listOfFiles) {
			if (file.isFile()) {
				try {
					System.out.println("Reading " + file.getName() + " file");
					String jsonData = readData(file.getName());
					storeMapList(file.getName(), documentParser.parse(jsonData));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return JsonMapList;
	}

	public List<String> searchIndex(Map<String, Map<String, String>> IndexedMap, String searchField) {
//		Instant start = Instant.now();
		List<String> resultFileNames = new ArrayList<String>();
		for (Entry<String, Map<String, String>> entry : IndexedMap.entrySet())  
		{
			if(entry.getValue().get(searchField) != null)
			{
//				if(entry.getValue().get(searchField).equals("private"))
//					System.out.println("aaa");
				resultFileNames.add(entry.getKey());
			}
			
		}
//		Instant finish = Instant.now();
//		main.timeElapsed = Duration.between(start, finish).toMillis();
		return resultFileNames;
 	}

	public Map<String, Map<String, String>> seperateMapEntries(Map<String, ArrayList<Map<String, String>>> JsonMapList,
			File[] listOfFiles) {
		StringBuffer sb = new StringBuffer();
		Map<String, Map<String, String>> IndexedMap = new HashMap<String, Map<String, String>>();
		for (File file : listOfFiles) {
			if (file.getName() != null) {
				ArrayList<Map<String, String>> MapList = JsonMapList.get(file.getName());
				if (MapList != null) {
					Iterator itr = MapList.iterator();
					while (itr.hasNext()) {
						Map<String, String> eachEntry = (Map<String, String>) itr.next();
						if (eachEntry != null) {
							sb.append(eachEntry.get("summary"));
							sb.append(eachEntry.get("description"));
						}
					}
				}
				
				IndexedMap = createInvertedIndex(sb, file.getName());
			}
		}
		return IndexedMap;
	}

	public Map<String, Map<String, String>> createInvertedIndex(StringBuffer sb, String fileName) {
		Map<String, String> fileSummaryDesc = new HashMap<String, String>();
		//Map<String, Map<String, String>> IndexedMap = new HashMap<String, Map<String, String>>();
		String[] individualString = sb.toString().toLowerCase().replaceAll("[-+./&^:\\,]", " ")
				.replaceAll("a, the, or, of", "").split(" ");
		Instant start = Instant.now();
		for (String string : individualString) {
			if (string.length() > 1)
				fileSummaryDesc.put(string, fileName);
		}
		Instant finish = Instant.now();
		main.indexTime += Duration.between(start, finish).toMillis();
		IndexedMap.put(fileName, fileSummaryDesc);
		return IndexedMap;

	}

	public void writeIndexedResult(Map<String, String> resultSet, String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		try {

			String json = mapper.writeValueAsString(resultSet);
			FileWriter fileWriter = new FileWriter(RESULT_FOLDER + "Indexed" + fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(json);
			printWriter.close();
			System.out.println("Transferred Indexes to a file ");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeResultSet(ArrayList<Map<String, String>> resultSet) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(resultSet);
			FileWriter fileWriter = new FileWriter(RESULT_FOLDER + "Result_Set");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(json);
			printWriter.close();
			System.out.println("Transferred Result set to a file ");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void storeMapList(String fileName, ArrayList<Map<String, String>> MapList) {
		JsonMapList.put(fileName, MapList);
	}
}
