package com.ssj.search.contract.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssj.search.contract.Searcher;

@Component("indexSearcher")
public class IndexSearch implements Searcher {

	@Override
	public List<String> searchKeywords(List<String> keyWords) {
		// TODO Auto-generated method stub
		return null;
	}

}