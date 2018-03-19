package com.ssj.search.controller;

import static com.ssj.search.util.ApplicationConstants.*;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssj.search.service.IndexSearchService;

/**
 * Rest Services Controller 
 * @author Sushil
 *
 */
@RestController
public class SearchServiceController {

	@Autowired
	IndexSearchService service;
	
	/**
	 * Search and return matching file names
	 * @param keywords - Words to search
	 * @param isFuzzy : Search is fuzzy or all words must be present in file, default is AND operation
	 * <TR> TRUE : OR will be performed
	 * <TR> FALSE : AND will be performed
	 * @return
	 */
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE},path="/getMatchingFiles")
	public List<String> getMatchingFiles(@RequestParam(name="keywords") String keywords, @RequestParam(required=false, name="isFuzzy", defaultValue="false") boolean isFuzzy){
		List<String> keyWordsList;
		if(keywords.contains(SEARCH_KEYWORD_SPLITTER)) {
			keyWordsList = Arrays.asList(keywords.split(SEARCH_KEYWORD_SPLITTER));
		}else {
			keyWordsList = Arrays.asList(keywords);
		}
		return service.searchMultipleWords(keyWordsList, isFuzzy);
	}

	/**
	 * Create an index for given directory
	 * @param path
	 * @return
	 */
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE},path="/buildIndexForDirectory")
	public String buildIndexForDirectory(@RequestParam String path){
		return service.indexDirectory(path);
	}
}
