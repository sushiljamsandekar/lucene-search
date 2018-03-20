package com.ssj.search.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Paths;

import org.apache.lucene.index.IndexWriter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class DocumentIndexAttributeTest {

	DocumentIndexAttribute dia;
	@MockBean
	IndexWriter iw;
	
	@Before
	public void setUp() throws Exception {
		dia = new DocumentIndexAttribute(iw, Paths.get(""), 1000);
	}
	
	@Test
	public void testDocumentIndexAttribute() {
		assertNotNull(dia);
	}

	@Test
	public void testGetWriter() {
		assertEquals(dia.getWriter(),iw);
	}

	@Test
	public void testGetFile() {
		assertEquals(dia.getFile(),Paths.get(""));
	}

	@Test
	public void testGetMillis() {
		assertEquals(dia.getMillis(),1000);
	}

}
