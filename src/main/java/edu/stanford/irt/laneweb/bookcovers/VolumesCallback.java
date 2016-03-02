package edu.stanford.irt.laneweb.bookcovers;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.books.model.Volumes;

public class VolumesCallback extends JsonBatchCallback<Volumes> {

    private static final Logger log = LoggerFactory.getLogger(VolumesCallback.class);

    private Integer bibid;

    private Map<Integer, String> thumbnailMap;

    public VolumesCallback(final Integer bibid, final Map<Integer, String> thumbnailMap) {
        this.bibid = bibid;
        this.thumbnailMap = thumbnailMap;
    }

    @Override
    public void onFailure(final GoogleJsonError e, final HttpHeaders responseHeaders) {
        log.warn(e.getMessage(), e);
    }

    @Override
    public void onSuccess(final Volumes volumes, final HttpHeaders responseHeaders) {
        if (volumes.getTotalItems() > 0 && !this.thumbnailMap.containsKey(this.bibid)) {
            Optional<String> url = volumes.getItems().stream()
                    .filter(v -> v.getVolumeInfo() != null && v.getVolumeInfo().getImageLinks() != null
                            && v.getVolumeInfo().getImageLinks().getSmallThumbnail() != null)
                    .map(v -> v.getVolumeInfo().getImageLinks().getSmallThumbnail()).findFirst();
            this.thumbnailMap.put(this.bibid, url.orElse(null));
        }
    }
}
