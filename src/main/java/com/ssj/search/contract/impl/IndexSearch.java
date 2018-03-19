package com.ssj.search.contract.impl;

import static com.ssj.search.util.ApplicationConstants.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssj.search.contract.IndexFields;
import com.ssj.search.contract.Searcher;

/**
 * Perform Search in already created index
 * @author Sushil
 *
 */
@Component("indexSearcher")
public class IndexSearch implements Searcher {

	private static final Logger logger = LoggerFactory.getLogger(IndexSearch.class);

	@Value("${search.index.directory}")
	private String indexPath;

	/**
	 * Search given keywords
	 * 
	 * @param keyWords : Words to be searched
	 * @param fuzzy : Search is fuzzy or all words must be present in file
	 * <TR> TRUE : OR will be performed
	 * <TR> FALSE : AND will be performed
	 */
	@Override
	public List<String> searchKeywords(List<String> keyWords, boolean fuzzy) {
		List<String> fileNames = null;
		try(IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))) {
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser(IndexFields.INDEX_FIELD_CONTENTS, analyzer);
			if (!fuzzy) {
				parser.setDefaultOperator(QueryParser.Operator.AND);
			}
			Query query = parser.parse(keyWords.stream().collect(Collectors.joining(" ")));
			TopDocs docs = searcher.search(query, 100);
			List<ScoreDoc> hitsList = Arrays.asList(docs.scoreDocs);
			fileNames = hitsList.stream().map(scoreDoc-> {
				try {
					return searcher.doc(scoreDoc.doc).get(IndexFields.INDEX_FIELD_PATH);
				} catch (IOException e) {
					//Return null if document is not present
					return null;
				}
			}).filter(e->e!=null).collect(Collectors.toList());
		} catch (IOException | ParseException e) {
			logger.error("Unable to search the keywords : ", e);
			return Arrays.asList(APP_CONST_ERROR_CODE_8001);
		}
		return fileNames;
	}

}
