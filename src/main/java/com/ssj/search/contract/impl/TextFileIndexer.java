package com.ssj.search.contract.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssj.search.contract.Indexer;
import com.ssj.search.exception.SearchException;

@Component
public class TextFileIndexer implements Indexer {

	private static final Logger logger = LoggerFactory.getLogger(TextFileIndexer.class);
	
	@Value("${search.index.directory}")
	private String indexPath;
	

	public boolean indexDocument(IndexWriter writer, Path file, long lastModified) throws SearchException {
	    try (InputStream stream = Files.newInputStream(file)) {
	        Document doc = new Document();

	        Field pathField = new StringField("path", file.toString(), Field.Store.YES);
	        doc.add(pathField);
	        doc.add(new LongPoint("modified", lastModified));
	        doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
	        
	        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
	          // New index, so we just add the document (no old document can be there):
	        	logger.debug("Creating Index : {} ", file);
	        	writer.addDocument(doc);
	        } else {
	        	logger.debug("Updating/Creating Index : {} ", file);
	        	writer.updateDocument(new Term("path", file.toString()), doc);
	        }
	      } catch (IOException e) {
			throw new SearchException("7003", file.toString());
		}
	    return Boolean.TRUE;
	}


	@Override
	public String getIndexDirectoryPath() {
		return indexPath;
	}
}
