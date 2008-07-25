package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.IdentifierCacheKey;
import org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

/**
 * This class extends ExpiresCachingProcessingPipeline to add the caching
 * that is broken for readers in that class.
 * @author ceyates
 *
 */
public class ExpiresPipeline extends ExpiresCachingProcessingPipeline {

    @Override
    protected boolean processReader(Environment environment)
            throws ProcessingException {
        //get the cache expiration value from the object model or as a parameter
        final Map objectModel = environment.getObjectModel();
        String expiresValue = (String)objectModel.get(CACHE_EXPIRES_KEY);
        if ( expiresValue == null ) {
            this.cacheExpires = this.parameters.getParameterAsLong("cache-expires", this.defaultCacheExpires);
        } else {
            this.cacheExpires = Long.valueOf(expiresValue).longValue();
            objectModel.remove(CACHE_EXPIRES_KEY);
        }
        
        //create the cache validity object
        if ( this.cacheExpires > 0) {
            this.cacheValidity = new ExpiresValidity(this.cacheExpires*1000);
        } else if ( this.cacheExpires < 0 ) {
            this.cacheValidity = NOPValidity.SHARED_INSTANCE;
        }
        
        //create the cache key
        String key = (String)objectModel.get(CACHE_KEY_KEY);
        if ( key == null ) {
            key = this.parameters.getParameter("cache-key", null);
            if ( key == null ) {
                key = environment.getURIPrefix()+environment.getURI();
            }
        } else {
            objectModel.remove(CACHE_KEY_KEY);
        }
        this.cacheKey = new IdentifierCacheKey(key,
                                           this.serializer == this.lastConsumer);
        
        //hand off
        return super.processReader(environment);
    }

}
