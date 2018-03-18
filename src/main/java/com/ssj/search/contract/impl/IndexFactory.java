package com.ssj.search.contract.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ssj.search.contract.FileTypesSupported;
import com.ssj.search.contract.Indexer;

@Component
public class IndexFactory {

	private static final Logger logger = LoggerFactory.getLogger(IndexFactory.class);
	@Autowired
	@Qualifier("textFileIndexer")
	private Indexer txtIndexer;
	
	public Indexer getIndexer(FileTypesSupported fileType) {
		logger.debug("getIndexer : Received fileType - {}",fileType);
		switch(fileType) {
			case TXT: return txtIndexer; 
			default: return txtIndexer;
		}
	}
}
