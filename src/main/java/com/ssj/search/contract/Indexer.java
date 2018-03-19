/**
 * 
 */
package com.ssj.search.contract;

import static com.ssj.search.util.ApplicationConstants.APP_CONST_ERROR_CODE_7001;
import static com.ssj.search.util.ApplicationConstants.APP_CONST_ERROR_CODE_7002;
import static com.ssj.search.util.ApplicationConstants.APP_CONST_ERROR_CODE_9001;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.ssj.search.exception.SearchException;
import com.ssj.search.util.DocumentIndexAttribute;
import com.ssj.search.util.DocumentIndexTask;

/**
 * Default methods are provided out of the box
 * Client has to give document write logic
 */
public interface Indexer {

	/**
	 * Index only file
	 * @param filePath
	 * @param createNewIndex
	 * @return
	 * @throws SearchException
	 */
	default String indexFile(String filePath, boolean createNewIndex) throws SearchException {
		return indexDirectory(filePath, createNewIndex);
	}

	/**
	 * Index all the files from directory
	 * @param directoryPath
	 * @param createNewIndex
	 * @return
	 * @throws SearchException
	 */
	default String indexDirectory(String directoryPath, boolean createNewIndex) throws SearchException {
		final Path docDir = Paths.get(directoryPath);
		if (!Files.isReadable(docDir)) {
		    throw new SearchException(APP_CONST_ERROR_CODE_7001,docDir.toAbsolutePath());
		}
		
		try(IndexWriter writer = getIndexWriter(createNewIndex)) {
			indexFiles(writer, docDir);
		}catch(IOException ioe) {
			throw new SearchException(APP_CONST_ERROR_CODE_7002,docDir.toAbsolutePath());
		}
		return APP_CONST_ERROR_CODE_9001;
	}
	
	/**
	 * Index all the files from given directory - visits all levels of the file directory structure
	 * @param writer
	 * @param path
	 * @throws IOException
	 * @throws SearchException
	 */
	default void indexFiles(IndexWriter writer, Path path) throws IOException, SearchException{
	    if (Files.isDirectory(path)) {
	    	List<DocumentIndexAttribute> documentsToBeIndexed=new ArrayList<>();
	    	
	        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
	          @Override
	          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	            //try {
	            	//indexDocument(writer, file, attrs.lastModifiedTime().toMillis());
	            	DocumentIndexAttribute dia = new DocumentIndexAttribute(writer, file, attrs.lastModifiedTime().toMillis());
	            	documentsToBeIndexed.add(dia);
	            //} catch (Exception ignore) {
	              // don't index files that can't be read.
	            //}
	            return FileVisitResult.CONTINUE;
	          }
	        });
	        if(documentsToBeIndexed.size()>0) {
	        	runInParallel(documentsToBeIndexed);
	        }
	      } else {
	    	  //Single file
	    	  indexDocument(writer, path, Files.getLastModifiedTime(path).toMillis());
	      }
	}

	/**
	 * Create and return the index writer
	 * <BR>IndexWriter instances are completely thread safe, meaning multiple threads can call any of its methods, 
	 * concurrently. If your application requires external synchronization, you should not synchronize on the 
	 * IndexWriter instance as this may cause deadlock; use your own (non-Lucene) objects instead
	 * @param createNewIndex
	 * @return
	 * @throws IOException
	 */
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
	
	/**
	 * Method must be threadsafe
	 * @param writer
	 * @param file
	 * @param millis
	 * @return
	 * @throws SearchException
	 */
	boolean indexDocument(IndexWriter writer, Path file, long millis) throws SearchException;
	public String getIndexDirectoryPath();
	
	/**
	 * If files are more, running the index operation in thread pool maximizes the performance
	 * @param documentsToBeIndexed
	 */
	default void runInParallel(List<DocumentIndexAttribute> documentsToBeIndexed) {
		ForkJoinPool pool = new ForkJoinPool();
		DocumentIndexTask tasks = new DocumentIndexTask(documentsToBeIndexed, this);
		pool.invoke(tasks);
	}
}
