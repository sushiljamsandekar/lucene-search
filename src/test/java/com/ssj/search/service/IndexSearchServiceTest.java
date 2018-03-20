package com.ssj.search.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ssj.search.BaseApplicationTest;
import com.ssj.search.contract.FileTypesSupported;
import com.ssj.search.contract.Indexer;
import com.ssj.search.contract.SearchSupported;
import com.ssj.search.contract.Searcher;
import com.ssj.search.contract.impl.IndexFactory;
import com.ssj.search.contract.impl.SearchFactory;
import com.ssj.search.exception.SearchException;

public class IndexSearchServiceTest extends BaseApplicationTest{

	@MockBean
	private IndexFactory indexFactory;
	@MockBean
	private SearchFactory searchFactory;
	@MockBean
	private Indexer indexer;
	@MockBean
	private Searcher searcher;
	private String dirPath = "/home/ssj/search/files";
	@InjectMocks
	private IndexSearchService service;
	List<String> words, files;
	
	@Before
	public void setUp() throws Exception {
		words = Arrays.asList("Test1");
		files = Arrays.asList("Filename1");
		MockitoAnnotations.initMocks(this);
		when(indexFactory.getIndexer(FileTypesSupported.TXT)).thenReturn(indexer);
		when(indexer.indexDirectory(dirPath, false)).thenReturn("9001");
		when(searchFactory.getSearcher(SearchSupported.INDEXSEARCH)).thenReturn(searcher);
		when(searcher.searchKeywords(words, false)).thenReturn(files);
	}

	@After
	public void tearDown() throws Exception {
		indexFactory = null;
		searchFactory = null;
		indexer = null;
	}

	@Test
	public void testIndexDirectory() throws SearchException {
		assertEquals("9001", service.indexDirectory(dirPath));
		when(indexer.indexDirectory(dirPath, false)).thenThrow(new SearchException());
		assertEquals("7002", service.indexDirectory(dirPath));
	}

	@Test
	public void testSearchMultipleWords() throws SearchException {
		//return searchFactory.getSearcher(SearchSupported.INDEXSEARCH).searchKeywords(words, fuzzy);
		assertEquals(files, service.searchMultipleWords(words, false));
		when(searcher.searchKeywords(words, false)).thenThrow(new SearchException());
		assertEquals("9001", service.indexDirectory(dirPath));
	}

}
