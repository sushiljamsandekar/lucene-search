package com.ssj.search.service;

import static com.ssj.search.util.ApplicationConstants.*;

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

/**
 * Service class for indexing and searching
 * @author Sushil
 *
 */
@Component
public class IndexSearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexSearchService.class);

	@Autowired
	private IndexFactory indexFactory;
	@Autowired
	private SearchFactory searchFactory;
	
	/**
	 * Index all files available in given directory
	 * <BR>Only text files are supported for indexing, future formats to follow
	 * @param directoryPath - Directory to get indexed
	 * @return
	 */
	public String indexDirectory(String directoryPath) {
		try {
			return indexFactory.getIndexer(FileTypesSupported.TXT).indexDirectory(directoryPath, false);
		} catch (SearchException e) {
			logger.error("Unable to Index : ",e);
		}
		return APP_CONST_ERROR_CODE_7002;
	}

	/**
	 * Search the given words
	 * @param words : Words to be searched
	 * @param fuzzy : Search is fuzzy or all words must be present in file
	 * <TR> TRUE : OR will be performed
	 * <TR> FALSE : AND will be performed
	 * @return
	 */
	public List<String> searchMultipleWords(List<String> words, boolean fuzzy) {
		try {
			return searchFactory.getSearcher(SearchSupported.INDEXSEARCH).searchKeywords(words, fuzzy);
		} catch (SearchException e) {
			logger.error("Unable to Index : ",e);
		}
		return Arrays.asList(APP_CONST_ERROR_CODE_8001);
	}
}
