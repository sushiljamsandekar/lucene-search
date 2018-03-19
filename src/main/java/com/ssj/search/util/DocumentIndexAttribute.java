package com.ssj.search.util;

import java.nio.file.Path;

import org.apache.lucene.index.IndexWriter;

public class DocumentIndexAttribute {

	private IndexWriter writer;
	
	public DocumentIndexAttribute(IndexWriter writer, Path file, long millis) {
		super();
		this.writer = writer;
		this.file = file;
		this.millis = millis;
	}
	private Path file;
	private long millis;
	
	public IndexWriter getWriter() {
		return writer;
	}
	public Path getFile() {
		return file;
	}
	public long getMillis() {
		return millis;
	}	
}
