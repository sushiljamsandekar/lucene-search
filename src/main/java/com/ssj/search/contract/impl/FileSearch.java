package com.ssj.search.contract.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssj.search.contract.Searcher;

/**
 * Search will not use Lucene Index but performs full table scan with mapreduce
 * @author Sushil
 *
 */
@Component("fileSearcher")
public class FileSearch implements Searcher {

	@Override
	public List<String> searchKeywords(List<String> keyWords, boolean isFuzzy) {
		// TODO Auto-generated method stub
		return null;
	}

}
