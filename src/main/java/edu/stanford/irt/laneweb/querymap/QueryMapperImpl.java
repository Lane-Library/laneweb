package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.DescriptorToResource;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.StreamResourceMapping;

public class QueryMapperImpl extends edu.stanford.irt.querymap.QueryMapper implements
        edu.stanford.irt.laneweb.querymap.QueryMapper {
    
    public QueryMapperImpl() {
        InputStream resourceMap = null;
        InputStream descriptorWeights = null;
        try {
            Context context = new InitialContext();
            String liveBase = (String) context.lookup("java:comp/env/live-base");
            resourceMap = new URL(liveBase + "/resources/querymap/resource-maps.xml").openStream();
            descriptorWeights = new URL(liveBase + "/resources/querymap/descriptor-weights.xml").openStream();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DescriptorManager descriptorManager = new DescriptorManager();
        super.setDescriptorManager(descriptorManager);
        QueryToDescriptor queryToDescriptor = new QueryToDescriptor();
        queryToDescriptor.setDescriptorManager(descriptorManager);
        queryToDescriptor.setHttpClient(new HttpClient(new MultiThreadedHttpConnectionManager()));
        setQueryToDescriptor(queryToDescriptor);
        DescriptorToResource descriptorToResource = new DescriptorToResource();
        descriptorToResource.setResourceMap(new StreamResourceMapping(resourceMap));
        queryToDescriptor.setDescriptorWeights(new DescriptorWeightMap(descriptorWeights));
        super.setDescriptorToResource(descriptorToResource);
    }

}
