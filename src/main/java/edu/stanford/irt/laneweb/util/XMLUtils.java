/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package edu.stanford.irt.laneweb.util;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * XML utility methods.
 * 
 * @version $Id: XMLUtils.java 587751 2007-10-24 02:41:36Z vgritsenko $
 */
public class XMLUtils {

    /**
     * Empty attributes immutable object.
     */
    public static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl() {

        @Override
        public void addAttribute(final String uri, final String localName, final String qName, final String type, final String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeAttribute(final int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAttribute(final int index, final String uri, final String localName, final String qName, final String type,
                final String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAttributes(final Attributes atts) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLocalName(final int index, final String localName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setQName(final int index, final String qName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setType(final int index, final String type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setURI(final int index, final String uri) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValue(final int index, final String value) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * Create a start and endElement without Attributes The content of the
     * Element is set to the stringValue parameter
     * 
     * @param localName
     *            The local name (without prefix)
     * @param stringValue
     *            The content of the Element
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void createElementNS(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName,
            final String stringValue) throws SAXException {
        startElement(xmlConsumer, namespaceURI, localName);
        data(xmlConsumer, stringValue);
        endElement(xmlConsumer, namespaceURI, localName);
    }

    /**
     * Add string data
     * 
     * @param xmlConsumer
     *            The SAX content handler
     * @param data
     *            The string data
     */
    public static void data(final XMLConsumer xmlConsumer, final String data) throws SAXException {
        xmlConsumer.characters(data.toCharArray(), 0, data.length());
    }

    /**
     * Create endElement Prefix must be mapped to empty String
     * <p>
     * For information on the names, see startElement.
     * </p>
     * 
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void endElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName)
            throws SAXException {
        xmlConsumer.endElement(namespaceURI, localName, localName);
    }

    /**
     * Create a startElement without Attributes Prefix must be mapped to empty
     * String
     * 
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void startElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName)
            throws SAXException {
        xmlConsumer.startElement(namespaceURI, localName, localName, EMPTY_ATTRIBUTES);
    }

    /**
     * Create a startElement with a empty Namespace Prefix must be mapped to
     * empty String
     * 
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @param atts
     *            The attributes attached to the element. If there are no
     *            attributes, it shall be an empty Attributes object.
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void startElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName,
            final Attributes atts) throws SAXException {
        xmlConsumer.startElement(namespaceURI, localName, localName, atts);
    }
}
