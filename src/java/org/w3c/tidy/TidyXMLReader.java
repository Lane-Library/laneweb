package org.w3c.tidy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public class TidyXMLReader implements XMLReader {

	private ContentHandler contentHandler;

	public ContentHandler getContentHandler() {
		return this.contentHandler;
	}

	public DTDHandler getDTDHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityResolver getEntityResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	public ErrorHandler getErrorHandler() {
		return new ErrorHandler() {

			public void error(SAXParseException exception) throws SAXException {
				System.out.println("error");
				exception.printStackTrace();
			}

			public void fatalError(SAXParseException exception)
					throws SAXException {
				System.out.println("fatalError");
				exception.printStackTrace();
				
			}

			public void warning(SAXParseException exception)
					throws SAXException {
				System.out.println("warning");
				exception.printStackTrace();
				
			}};
	}

	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void parse(InputSource input) throws IOException, SAXException {
		parse(input.getByteStream());
	}

	/**
	 * @param byteStream
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	private void parse(InputStream byteStream) throws IOException, SAXException {
        Configuration configuration = new Configuration();
        TagTable tt = new TagTable();
        tt.setConfiguration(configuration);
        configuration.tt = tt;
		configuration.XmlOut = true;
		Properties properties = new Properties();
		properties.setProperty("new-blocklevel-tags", "xi:include xi:fallback");
		configuration.addProps(properties);
        configuration.NumEntities = true;
        configuration.CharEncoding = Configuration.UTF8;
        configuration.adjust();
        StreamIn streams = new StreamInImpl(byteStream, configuration.CharEncoding, 2);
        Lexer lexer = new Lexer(streams,configuration);
        lexer.errout = new PrintWriter(System.err);
        final Node node = ParserImpl.parseDocument(lexer);
        Clean cleaner = new Clean(configuration.tt);
        cleaner.cleanTree(lexer, node);
        final NodeParser nodeParser = new NodeParser();
        nodeParser.setContentHandler(this.contentHandler);
        nodeParser.setLexicalHandler((LexicalHandler) this.contentHandler);
        if (!node.checkNodeIntegrity())
        {
                throw new SAXException("bad tree");
        }
        nodeParser.parseNode((short) 0, node);
	}

	public void parse(String systemId) throws IOException, SAXException {
		// TODO Auto-generated method stub

	}

	public void setContentHandler(ContentHandler handler) {
		this.contentHandler = handler;
	}

	public void setDTDHandler(DTDHandler handler) {
		// TODO Auto-generated method stub

	}

	public void setEntityResolver(EntityResolver resolver) {
		// TODO Auto-generated method stub

	}

	public void setErrorHandler(ErrorHandler handler) {
		// TODO Auto-generated method stub

	}

	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		// TODO Auto-generated method stub

	}

	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		// TODO Auto-generated method stub

	}
	static class TestLexicalHandler implements LexicalHandler {

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
		 */
		public void comment(char[] ch, int start, int length)
				throws SAXException {
			System.out.println("comment("+new String(ch,start,length)+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
		 */
		public void endCDATA() throws SAXException {
			System.out.println("endCDATA()");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#endDTD()
		 */
		public void endDTD() throws SAXException {
			System.out.println("endDTD()");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
		 */
		public void endEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
		 */
		public void startCDATA() throws SAXException {
			System.out.println("startCDATA()");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
		 */
		public void startDTD(String name, String publicId, String systemId)
				throws SAXException {
			System.out.println("startDTD("+name+","+publicId+","+systemId+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
		 */
		public void startEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}};
	static class TestContentHandler implements ContentHandler {

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			System.out.println("characters("+new String(ch,start,length)+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		public void endDocument() throws SAXException {
			System.out.println("endDocument()");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			System.out.println("endElement("+uri+","+localName+","+name+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
		 */
		public void endPrefixMapping(String prefix) throws SAXException {
			System.out.println("endPrefixMapping("+prefix+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
		 */
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
		 */
		public void processingInstruction(String target, String data)
				throws SAXException {
			System.out.println("processingInstruction("+target+","+data+")");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
		 */
		public void setDocumentLocator(Locator locator) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
		 */
		public void skippedEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startDocument()
		 */
		public void startDocument() throws SAXException {
			System.out.println("startDocument()");
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String uri, String localName, String name,
				Attributes atts) throws SAXException {
			System.out.println("startElement("+uri+","+localName+","+name+")");
			for (int i = 0; i < atts.getLength(); i++) {
				System.out.println("    "+atts.getLocalName(i)+"="+atts.getValue(i));
			}
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
		 */
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			System.out.println("startPrefixMapping("+prefix+","+uri+")");
		}
		
	}

}
