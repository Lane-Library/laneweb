package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.CollectionManager;

public interface BassettCollectionManager extends CollectionManager {
    
    Collection<Eresource> getById(String id);

}
