/*
 * Created on Nov 6, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb.xslt;

import java.util.StringTokenizer;

import javax.xml.transform.TransformerException;

import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XObjectFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author ceyates
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LanewebXSLTExtensions {

    public static boolean contains(ExpressionContext context, Object obj1, Object obj2) throws TransformerException {
        XObject arg1 = XObjectFactory.create(obj1, context.getXPathContext());
        XObject arg2 = XObjectFactory.create(obj2, context.getXPathContext());
        XMLString nodeValue = arg1.xstr().fixWhiteSpace(true, true, true).toUpperCase();
        XMLString keywords = arg2.xstr().fixWhiteSpace(true,true,true).toUpperCase();
        for (StringTokenizer st = new StringTokenizer(keywords.toString());st.hasMoreTokens();) {
            String token = st.nextToken();
            int index = nodeValue.indexOf(token);
            if (index < 0 || (index != 0 && nodeValue.charAt(index-1) != ' ')) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean contains(NodeList list, String keywords) {
        if (list.getLength() > 1) throw new RuntimeException("this is broken");
        Node node = list.item(0);
        for (StringTokenizer st = new StringTokenizer(keywords);st.hasMoreTokens();) {
            if (!contains(node,st.nextToken().toUpperCase())) {
                return false;
            }
        }
	    return true;
    }
    
    private static boolean contains(Node node, String keyword) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            Text text = (Text) node;
            String data = text.getData().toUpperCase();
            int index = data.indexOf(keyword);
            return index >= 0 && (index ==0 || data.charAt(index-1) == ' ');
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                if  (contains(list.item(i),keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

}
