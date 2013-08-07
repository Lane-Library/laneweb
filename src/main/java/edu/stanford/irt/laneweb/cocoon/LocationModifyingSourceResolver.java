package edu.stanford.irt.laneweb.cocoon;

import java.net.URI;
import java.net.URISyntaxException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolverImpl;
import edu.stanford.irt.laneweb.LanewebException;

public class LocationModifyingSourceResolver extends SourceResolverImpl {

    private LocationModifier modifier = new LocationModifier();

    @Override
    public Source resolveURI(final URI location) {
        try {
            return super.resolveURI(this.modifier.modify(location));
        } catch (URISyntaxException e) {
            throw new LanewebException(e);
        }
    }
}
