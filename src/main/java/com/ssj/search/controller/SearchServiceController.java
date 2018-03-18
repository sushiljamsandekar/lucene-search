package com.ssj.search.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssj.search.service.IndexSearchService;

@RestController
public class SearchServiceController {

	@Autowired
	IndexSearchService service;
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},path="/getMatchingFiles")
	public List<String> getMatchingFiles(@RequestParam String keywords){
		return service.searchMultipleWords(Arrays.asList(keywords.split(",")));
	}

	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},path="/buildIndexForDirectory")
	public String buildIndexForDirectory(@RequestParam String path){
		return service.indexDirectory(path);
	}
}
