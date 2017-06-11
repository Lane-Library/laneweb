package edu.stanford.irt.laneweb.metasearch;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import edu.stanford.irt.search.impl.Result;

public interface MetaSearchService {

    void clearAllCaches();

    void clearCache(String q);

    Result describe(String query, Collection<String> engines);

    HttpResponse execute(HttpGet httpGet) throws IOException;

    Result search(String query, Collection<String> engines, long wait);
}