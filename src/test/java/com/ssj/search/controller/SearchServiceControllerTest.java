package com.ssj.search.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ssj.search.BaseApplicationTest;
import com.ssj.search.service.IndexSearchService;

@WebMvcTest(SearchServiceController.class)
public class SearchServiceControllerTest extends BaseApplicationTest{

	@Autowired
	private MockMvc mvc;
	@MockBean
	private IndexSearchService service;
	@MockBean
	private SearchServiceController searchServiceController;
	
	@Before
	public void setUp() throws Exception {
		List<String> matchedFiles = Arrays.asList("//a//b///c//File1.txt");
		given(service.searchMultipleWords(Arrays.asList("First"), false)).willReturn(matchedFiles);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetMatchingFiles() throws Exception {
		mvc.perform(get("/getMatchingFiles")
				.param("keywords", "First")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());	
	}

	@Test
	public void testBuildIndexForDirectory() throws Exception {
		mvc.perform(get("/buildIndexForDirectory")
				.param("path", "/home/ssj/search/files")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());	
	}
}
