package edu.stanford.irt.laneweb.tidy;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.tidy.TidyXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import junit.framework.TestCase;

public class TidyXMLReaderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testParseInputSource() throws IOException, SAXException, TransformerException {
//		XMLReader reader = new TidyXMLReader();
////		InputSource input = new InputSource(new FileInputStream(" "));
////		reader.parse(input);
//		TransformerFactory fact = TransformerFactory.newInstance();
//		Transformer transformer = fact.newTransformer();
//		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//		transformer.setErrorListener(new ErrorListener(){
//
//			public void error(TransformerException exception)
//					throws TransformerException {
//				System.out.println(exception. getMessage());
//				exception.printStackTrace();
//			}
//
//			public void fatalError(TransformerException exception)
//					throws TransformerException {
//				exception.printStackTrace();
//			}
//
//			public void warning(TransformerException exception)
//					throws TransformerException {
//				exception.printStackTrace();
//			}});
//		transformer.transform(new SAXSource(reader,new InputSource(new FileInputStream("/Users/ceyates/Documents/workspace/laneweb-content/content/search2.html"))), new StreamResult(System.out));
	}

}
