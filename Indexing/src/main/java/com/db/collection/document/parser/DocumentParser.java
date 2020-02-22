package com.db.collection.document.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocumentParser {

	public static DocumentParser instance;
	public static final TypeReference <ArrayList<Map<String, Object>>> TYPE = new TypeReference<ArrayList<Map<String, Object>>>() {
	};

	private DocumentParser() {
	}

	public static DocumentParser getInstance() {
		if (instance == null)
			instance = new DocumentParser();
		return instance;
	}

	public ArrayList<Map<String, String>> parse(String jsonData) throws IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<Map<String, String>> jsonMap = objectMapper.readValue(jsonData, TYPE);
		return jsonMap;

	}

}
