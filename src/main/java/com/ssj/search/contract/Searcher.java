package com.ssj.search.contract;

import java.util.List;

import com.ssj.search.exception.SearchException;

public interface Searcher {

	public List<String> searchKeywords(List<String> keyWords) throws SearchException;
}
