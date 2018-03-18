package com.ssj.search.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssj.search.contract.FileTypesSupported;
import com.ssj.search.contract.SearchSupported;
import com.ssj.search.contract.impl.IndexFactory;
import com.ssj.search.contract.impl.SearchFactory;
import com.ssj.search.exception.SearchException;

@Component
public class IndexSearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexSearchService.class);

	@Autowired
	private IndexFactory indexFactory;
	private SearchFactory searchFactory;
	
	public String indexDirectory(String directoryPath) {
		try {
			return indexFactory.getIndexer(FileTypesSupported.TXT).indexDirectory(directoryPath, false);
		} catch (SearchException e) {
			logger.error("Unable to Index : ",e);
		}
		return "7002";
	}

	public List<String> searchMultipleWords(List<String> words) {
		try {
			return searchFactory.getSearcher(SearchSupported.INDEXSEARCH).searchKeywords(words);
		} catch (SearchException e) {
			logger.error("Unable to Index : ",e);
		}
		return Arrays.asList("8001");
	}
}
