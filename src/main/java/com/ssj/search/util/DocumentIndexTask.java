package com.ssj.search.util;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import com.ssj.search.contract.Indexer;
import com.ssj.search.exception.SearchException;

public class DocumentIndexTask extends RecursiveAction {

	/**
	 * Default Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	protected static int sThreshold = 1;
	
	private List<DocumentIndexAttribute> documentsToBeIndexed;
	private Indexer indexClient;
	
	public DocumentIndexTask(List<DocumentIndexAttribute> documentsToBeIndexed, Indexer indexClient){
		this.documentsToBeIndexed = documentsToBeIndexed;
		this.indexClient = indexClient;
	}
	
	@Override
	protected void compute() {
		 if (documentsToBeIndexed.size() == sThreshold) {
			 indexDirectly(documentsToBeIndexed.get(0));
		        return;
		    }
		    
		    int split = documentsToBeIndexed.size() / 2;
		    invokeAll(new DocumentIndexTask(documentsToBeIndexed.subList(0, split), this.indexClient),
		              new DocumentIndexTask(documentsToBeIndexed.subList(split, documentsToBeIndexed.size()), this.indexClient));
	}

	protected void indexDirectly(DocumentIndexAttribute attributes) {
		try {
			indexClient.indexDocument(attributes.getWriter(), attributes.getFile(), attributes.getMillis());
		} catch (SearchException e) {
			//don't index files that can't be read.
		}
	}
}
