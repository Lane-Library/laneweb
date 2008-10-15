package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.BassettEresource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLLizableBassettEresourceList    implements XMLizable{

	Collection<Eresource> bassetts; 
	
	Map<String, Map> regionCounter = new HashMap<String, Map>();


    private String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String BASSETTS = "bassetts";
    private static final String BASSETT = "bassett";
    private static final String BASSETT_NUMBER = "bassett_number";
    private static final String BASSETT_IMAGE = "bassett_image";
    private static final String DIAGRAM = "diagram_image";
    private static final String LEGEND_IMAGE = "legend_image";
    private static final String LEGEND = "legend";
    private static final String REGIONS = "regions";
    private static final String REGION = "region";
    private static final String SUB_REGION = "sub_region";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    
    private static final String LINK = "link";
    
    
    private static final String NAME = "name";
    
    public XMLLizableBassettEresourceList(Collection<Eresource> bassetts)
	{
    	this.bassetts = bassetts;
	}
	
	

    public void toSAX(final ContentHandler consumer) throws SAXException {
        consumer.startPrefixMapping("", NAMESPACE);
        XMLUtils.startElement(consumer, NAMESPACE, BASSETTS);
        if(this.bassetts!= null)
        {
	        for (Eresource eresource : this.bassetts) {
	            handleEresource(consumer, eresource);
	        }
	    }
        XMLUtils.endElement(consumer, NAMESPACE, BASSETTS);
        consumer.endPrefixMapping("");
        
    }
        
        

        private void handleEresource(final ContentHandler handler,
                final Eresource eresource) throws SAXException {
            BassettEresource bassett = (BassettEresource) eresource;
        
            AttributesImpl attributes = new AttributesImpl();
		    attributes.addAttribute(NAMESPACE, BASSETT_NUMBER, BASSETT_NUMBER, "CDATA", bassett.getBassettNumber());    
	    	XMLUtils.startElement(handler, NAMESPACE, BASSETT, attributes);
                
            XMLUtils.startElement(handler, NAMESPACE, TITLE);
            XMLUtils.data(handler, bassett.getTitle());
            XMLUtils.endElement(handler, NAMESPACE, TITLE);
            
            XMLUtils.startElement(handler, NAMESPACE, BASSETT_IMAGE );
            XMLUtils.data(handler, bassett.getImage());
            XMLUtils.endElement(handler, NAMESPACE, BASSETT_IMAGE);
   
            XMLUtils.startElement(handler, NAMESPACE, DIAGRAM );
            XMLUtils.data(handler, bassett.getDiagram());
            XMLUtils.endElement(handler, NAMESPACE, DIAGRAM);
   
            XMLUtils.startElement(handler, NAMESPACE, LEGEND_IMAGE );
            XMLUtils.data(handler, bassett.getLatinLegend());
            XMLUtils.endElement(handler, NAMESPACE, LEGEND_IMAGE );
            if(null != bassett.getEngishLegend()) {
	            XMLUtils.startElement(handler, NAMESPACE, LEGEND );
	            XMLUtils.data(handler, bassett.getEngishLegend());
	            XMLUtils.endElement(handler, NAMESPACE, LEGEND );
            }
            if(null != bassett.getDescription()) {
	            XMLUtils.startElement(handler, NAMESPACE, DESCRIPTION );
	            XMLUtils.data(handler, bassett.getDescription());
	            XMLUtils.endElement(handler, NAMESPACE, DESCRIPTION );
            } 
            XMLUtils.startElement(handler, NAMESPACE, LINK );
            if(bassett.getVersions().size() >0 )
            {
            	Version version = bassett.getVersions().iterator().next();
            	if(version.getLinks().size() >0)
            		XMLUtils.data(handler, version.getLinks().iterator().next().getUrl());
            }
            XMLUtils.endElement(handler, NAMESPACE, LINK );
   
            handleRegion(handler, bassett.getRegions());
            XMLUtils.endElement(handler, NAMESPACE, BASSETT);    
        }

        private void handleRegion(final ContentHandler handler, List<String> regions) throws SAXException {
        	String currentRegion = null;
        	XMLUtils.startElement(handler, NAMESPACE, REGIONS );
        	for (int i=0;  i< regions.size(); i++) {
        		String region  = regions.get(i);
        		String[] splittedRegion  = region.split("--");
        		if(!splittedRegion[0].equals(currentRegion)) 
				{
        			if(i != 0)
        				XMLUtils.endElement(handler, NAMESPACE, REGION );
        			AttributesImpl attributes = new AttributesImpl();
    			    attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", splittedRegion[0]);
    			    XMLUtils.startElement(handler, NAMESPACE, REGION, attributes);
		            currentRegion = splittedRegion[0];
			   }
        		if(splittedRegion.length >1)
				{
					XMLUtils.startElement(handler, NAMESPACE, SUB_REGION );
		            XMLUtils.data(handler, splittedRegion[1] );
				    XMLUtils.endElement(handler, NAMESPACE, SUB_REGION );
				}
    		}
        	XMLUtils.endElement(handler, NAMESPACE, REGION );
	    	XMLUtils.endElement(handler, NAMESPACE, REGIONS );
        }
	
}

