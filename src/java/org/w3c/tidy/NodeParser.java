/*
 *  Java HTML Tidy - JTidy
 *  HTML parser and pretty printer
 *
 *  Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 *  Institute of Technology, Institut National de Recherche en
 *  Informatique et en Automatique, Keio University). All Rights
 *  Reserved.
 *
 *  Contributing Author(s):
 *
 *     Dave Raggett <dsr@w3.org>
 *     Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 *     Gary L Peskin <garyp@firstech.com> (Java development)
 *     Sami Lempinen <sami@lempinen.net> (release management)
 *     Fabrizio Giustina <fgiust at users.sourceforge.net>
 *
 *  The contributing author(s) would like to thank all those who
 *  helped with testing, bug fixes, and patience.  This wouldn't
 *  have been possible without all of you.
 *
 *  COPYRIGHT NOTICE:
 *
 *  This software and documentation is provided "as is," and
 *  the copyright holders and contributing author(s) make no
 *  representations or warranties, express or implied, including
 *  but not limited to, warranties of merchantability or fitness
 *  for any particular purpose or that the use of the software or
 *  documentation will not infringe any third party patents,
 *  copyrights, trademarks or other rights.
 *
 *  The copyright holders and contributing author(s) will not be
 *  liable for any direct, indirect, special or consequential damages
 *  arising out of any use of the software or documentation, even if
 *  advised of the possibility of such damage.
 *
 *  Permission is hereby granted to use, copy, modify, and distribute
 *  this source code, or portions hereof, documentation and executables,
 *  for any purpose, without fee, subject to the following restrictions:
 *
 *  1. The origin of this source code must not be misrepresented.
 *  2. Altered versions must be plainly marked as such and must
 *     not be misrepresented as being the original source.
 *  3. This Copyright notice may not be removed or altered from any
 *     source or altered source distribution.
 *
 *  The copyright holders and contributing author(s) specifically
 *  permit, without fee, and encourage the use of this source code
 *  as a component for supporting the Hypertext Markup Language in
 *  commercial products. If you use this source code in a product,
 *  acknowledgment is not required but would be appreciated.
 *
 */
package org.w3c.tidy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Stolen from org.w3c.tidy.PPrint.java
 */
public class NodeParser {

    private ContentHandler contentHandler;

    private LexicalHandler lexicalHandler;

    private Map<String, String> prefixMapping = new HashMap<String, String>();

    public void setContentHandler(final ContentHandler contentHandler) {
        if (null == contentHandler) {
            throw new IllegalArgumentException("null contentHandler");
        }
        this.contentHandler = contentHandler;
    }

    public void setLexicalHandler(final LexicalHandler lexicalHandler) {
        if (null == lexicalHandler) {
            throw new IllegalArgumentException("null lexicalHandler");
        }
        this.lexicalHandler = lexicalHandler;
    }

    /**
     * @param mode
     * @param indent
     * @param lexer
     * @param node
     * @throws IOException
     * @throws SAXException
     */
    public void parseNode(final short mode, final Node node) throws IOException, SAXException {

        if (node == null) {
            return;
        }

        if (node.type == Node.TextNode) {
            char[] chars = new String(node.textarray, node.start, node.end - node.start, "UTF-8").toCharArray();
            this.contentHandler.characters(chars, 0, chars.length);
        } else if (node.type == Node.CommentTag) {
            char[] chars = new String(node.textarray, node.start, node.end - node.start, "UTF-8").toCharArray();
            this.lexicalHandler.comment(chars, 0, chars.length);
        } else if (node.type == Node.RootNode) {
            this.contentHandler.startDocument();
            Node content;

            for (content = node.content; content != null; content = content.next) {
                parseNode(mode, content);
            }
            this.contentHandler.endDocument();
        } else if (node.type == Node.DocTypeTag) {
            String doctypeString = new String(node.textarray, node.start, node.end - node.start);
            Pattern pattern = Pattern.compile("\\\"");
            String[] matches = pattern.split(doctypeString.subSequence(0, doctypeString.length()));
            String publicId = null, systemId = null, name = null;
            name = (matches[0].substring(0, matches[0].indexOf(' ')));
            if (matches[0].indexOf("PUBLIC") > 0) {
                publicId = matches[1];
                systemId = matches[3];
            } else if (matches[0].indexOf("SYSTEM") > 0) {
                systemId = matches[1];
            }
            this.lexicalHandler.startDTD(name, publicId, systemId);
            this.lexicalHandler.endDTD();
        } else if (node.type == Node.ProcInsTag) {
            String pi = new String(node.textarray, node.start, node.end - node.start);
            if (pi.indexOf(' ') > 0) {
                String name = pi.substring(0, pi.indexOf(' '));
                String value = pi.substring(pi.indexOf(' ') + 1, pi.length() - 1);// last
                // '?'
                // appears
                // in
                // string
                this.contentHandler.processingInstruction(name, value);
            }
        }
        // else if (node.type == Node.XML_DECL)
        // {
        // return;
        // }
        else if (node.type == Node.CDATATag) {
            char[] chars = new String(node.textarray, node.start, node.end - node.start, "UTF-8").toCharArray();
            this.lexicalHandler.startCDATA();
            this.contentHandler.characters(chars, 0, chars.length);
            this.lexicalHandler.endCDATA();
        } else if (node.type == Node.SectionTag) {
            return;
        } else if ((node.tag.model & Dict.CM_EMPTY) != 0 || node.type == Node.StartEndTag) {
            AttributesImpl outAtts = new AttributesImpl();
            AttVal atts = node.attributes;
            String prefix = null;
            String uri = null;
            while (atts != null) {
                if (atts.attribute.indexOf("xmlns") == 0) {
                    prefix =
                            atts.attribute.indexOf(':') > 0 ? atts.attribute.substring(atts.attribute.indexOf(':') + 1,
                                                                                       atts.attribute.length()) : "";
                    uri = atts.value;
                    this.prefixMapping.put(prefix, uri);
                } else {
                    if (null == atts.value) {
                        atts.value = atts.attribute;
                    }
                    outAtts.addAttribute("", atts.attribute, atts.attribute, "CDATA", atts.value);
                }
                atts = atts.next;
            }
            if (prefix != null) {
                this.contentHandler.startPrefixMapping(prefix, uri);
            }
            String ns = this.prefixMapping.get("");
            String localName = node.element;
            if (localName.indexOf(':') > 0) {
                ns = this.prefixMapping.get(localName.substring(0, localName.indexOf(':')));
                localName = localName.substring(localName.indexOf(':') + 1);
            }
            this.contentHandler.startElement(ns, localName, node.element, outAtts);
            this.contentHandler.endElement(ns, localName, node.element);
            if (prefix != null) {
                this.contentHandler.endPrefixMapping(prefix);
            }
        } else {
            // some kind of container element
            Node content;

            for (content = node.content; content != null; content = content.next) {
                if (content.type == Node.TextNode) {
                    break;
                }
            }

            AttributesImpl outAtts = new AttributesImpl();
            AttVal atts = node.attributes;
            String prefix = null;
            String uri = null;
            while (atts != null) {
                if (atts.attribute.indexOf("xmlns") == 0) {
                    prefix =
                            atts.attribute.indexOf(':') > 0 ? atts.attribute.substring(atts.attribute.indexOf(':') + 1,
                                                                                       atts.attribute.length()) : "";
                    uri = atts.value;
                    this.prefixMapping.put(prefix, uri);
                } else {
                    if (null == atts.value) {
                        atts.value = atts.attribute;
                    }
                    outAtts.addAttribute("", atts.attribute, atts.attribute, "CDATA", atts.value);
                }
                atts = atts.next;
            }
            if (prefix != null) {
                this.contentHandler.startPrefixMapping(prefix, uri);
            }
            String ns = this.prefixMapping.get("");
            String localName = node.element;
            if (localName.indexOf(':') > 0) {
                ns = this.prefixMapping.get(localName.substring(0, localName.indexOf(':')));
                localName = localName.substring(localName.indexOf(':') + 1);
            }
            this.contentHandler.startElement(ns, localName, node.element, outAtts);

            for (content = node.content; content != null; content = content.next) {
                parseNode(mode, content);
            }

            this.contentHandler.endElement(ns, localName, node.element);
            if (prefix != null) {
                this.contentHandler.endPrefixMapping(prefix);
            }

        }
    }

}