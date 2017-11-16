package edu.stanford.irt.laneweb.suggest;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;
import edu.stanford.irt.status.StatusProvider;
import edu.stanford.irt.suggest.SuggestionManager;

public class SuggestStatusProvider implements StatusProvider {

    private static final String FAIL_FORMAT = "suggestion status failed in %dms: %s";

    private static final Logger log = LoggerFactory.getLogger(SuggestStatusProvider.class);

    private static final String SUCCESS_FORMAT = "suggestions took %dms.";

    private int maxOKTime;

    private SuggestionManager suggestionManager;

    private String term;

    public SuggestStatusProvider(final SuggestionManager suggestionManager, final int maxOKTime, final String term) {
        this.suggestionManager = suggestionManager;
        this.maxOKTime = maxOKTime;
        this.term = term;
    }

    @Override
    public List<StatusItem> getStatusItems() {
        Status status;
        String message;
        long time;
        Instant start = Instant.now();
        try {
            this.suggestionManager.getSuggestionsForTerm(this.term);
            time = Duration.between(start, Instant.now()).toMillis();
            status = time < this.maxOKTime ? Status.OK : Status.WARN;
            message = String.format(SUCCESS_FORMAT, time);
        } catch (RuntimeException e) {
            status = Status.ERROR;
            time = Duration.between(start, Instant.now()).toMillis();
            message = String.format(FAIL_FORMAT, time, e);
            log.error(message);
        }
        return Collections.singletonList(new StatusItem(status, message));
    }
}
