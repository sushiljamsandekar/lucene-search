package com.ssj.search.contract.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ssj.search.contract.SearchSupported;
import com.ssj.search.contract.Searcher;

@Component
public class SearchFactory {

	private static final Logger logger = LoggerFactory.getLogger(IndexFactory.class);
	@Autowired
	@Qualifier("fileSearcher")
	private Searcher fileSearch;

	@Autowired
	@Qualifier("indexSearcher")
	private Searcher indexSearch;
	
	public Searcher getSearcher(SearchSupported searchType) {
		logger.debug("getSearcher : Received Search Type - {}",searchType);
		switch(searchType) {
			case FILESEARCH: return fileSearch; 
			case INDEXSEARCH: return indexSearch;
			default: return indexSearch;
		}
	}
}
