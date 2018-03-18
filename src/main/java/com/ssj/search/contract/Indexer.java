/**
 * 
 */
package com.ssj.search.contract;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.ssj.search.exception.SearchException;

/**
 * 
 *
 */
public interface Indexer {

	default String indexFile(String filePath, boolean createNewIndex) throws SearchException {
		return indexDirectory(filePath, createNewIndex);
	}

	default String indexDirectory(String directoryPath, boolean createNewIndex) throws SearchException {
		final Path docDir = Paths.get(directoryPath);
		if (!Files.isReadable(docDir)) {
		    throw new SearchException("7001",docDir.toAbsolutePath());
		}
		
		try(IndexWriter writer = getIndexWriter(createNewIndex)) {
			indexFiles(writer, docDir);
		}catch(IOException ioe) {
			throw new SearchException("7002",docDir.toAbsolutePath());
		}
		return "9001";
	}
	
	default void indexFiles(IndexWriter writer, Path path) throws IOException, SearchException{
	    if (Files.isDirectory(path)) {
	        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
	          @Override
	          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	            try {
	            	indexDocument(writer, file, attrs.lastModifiedTime().toMillis());
	            } catch (SearchException ignore) {
	              // don't index files that can't be read.
	            }
	            return FileVisitResult.CONTINUE;
	          }
	        });
	      } else {
	    	  indexDocument(writer, path, Files.getLastModifiedTime(path).toMillis());
	      }
	}

	default IndexWriter getIndexWriter(boolean createNewIndex) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(getIndexDirectoryPath()));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		if(createNewIndex) {
			iwc.setOpenMode(OpenMode.CREATE);
		}else {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		return new IndexWriter(dir, iwc);
	}
	
	boolean indexDocument(IndexWriter writer, Path file, long millis) throws SearchException;
	public String getIndexDirectoryPath();
}
