package edu.stanford.irt.laneweb.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class SpamFilter {

    // one hour
    private static final long COUNT_CHECK_INTERVAL = 1000L * 60L * 60L;

    private static final int MAX_MAILS_PER_IP = 10;

    private static final String RECIPIENT = "recipient";

    private long lastUpdate = 0;

    private Set<String> recipients;

    private Map<String, Integer> sentMailCounter = new HashMap<>();

    private Set<String> spamIps;

    private Set<String> spamReferrers;

    public SpamFilter(final Set<String> recipients, final Set<String> spamIps, final Set<String> spamReferrers) {
        this.recipients = recipients;
        this.spamIps = spamIps;
        this.spamReferrers = spamReferrers;
    }

    public void addSpamIP(final String spamIP) {
        this.spamIps.add(spamIP);
    }

    public void addSpamReferrer(final String spamReferrer) {
        this.spamReferrers.add(spamReferrer);
    }

    public void checkForSpam(final Map<String, Object> map) {
        Object recipient = map.get(RECIPIENT);
        if (!this.recipients.contains(recipient)) {
            throw new LanewebException(RECIPIENT + " " + recipient + " not permitted");
        }
        Object remoteIp = map.get(Model.REMOTE_ADDR);
        if (this.spamIps.contains(remoteIp)) {
            throw new LanewebException(remoteIp + " is in the spam IP list");
        }
        Object referrer = map.get(Model.REFERRER);
        if (this.spamReferrers.contains(referrer)) {
            throw new LanewebException(referrer + " is in the spam referrer list");
        }
        maybeBlockBeforeSend((String) map.get(Model.REMOTE_ADDR));
    }

    public boolean removeSpamIP(final String spamIP) {
        return this.spamIps.remove(spamIP);
    }

    public boolean removeSpamReferrer(final String spamReferrer) {
        return this.spamReferrers.remove(spamReferrer);
    }

    private synchronized void maybeBlockBeforeSend(final String remoteIp) {
        updateSentCountsIfNecessary();
        int sent = 0;
        if (this.sentMailCounter.containsKey(remoteIp)) {
            sent = this.sentMailCounter.get(remoteIp).intValue();
        }
        this.sentMailCounter.put(remoteIp, Integer.valueOf(++sent));
        if (sent > MAX_MAILS_PER_IP) {
            throw new LanewebException("too many emails from IP: " + remoteIp + "; # sent: " + sent);
        }
    }

    private synchronized void updateSentCountsIfNecessary() {
        long now = System.currentTimeMillis();
        if (now > this.lastUpdate + COUNT_CHECK_INTERVAL) {
            List<String> entriesToRemove = new ArrayList<>();
            for (Entry<String, Integer> entry : this.sentMailCounter.entrySet()) {
                int newCount = entry.getValue().intValue() - MAX_MAILS_PER_IP;
                if (newCount <= 0) {
                    entriesToRemove.add(entry.getKey());
                } else {
                    entry.setValue(Integer.valueOf(newCount));
                }
            }
            for (String key : entriesToRemove) {
                this.sentMailCounter.remove(key);
            }
            this.lastUpdate = now;
        }
    }
}
