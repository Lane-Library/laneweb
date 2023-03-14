package edu.stanford.irt.laneweb.trends.googleA4;

import java.util.ArrayList;
import java.util.List;

public class Payload {

    String client_id;

    boolean non_personalized_ads = false;

    List<Event> events;

    public Payload(String client_id) {
        this.client_id = client_id;
        this.events = new ArrayList<Event>();
    }

    public boolean isNon_personalized_ads() {
        return non_personalized_ads;
    }

    public void setNon_personalized_ads(boolean non_personalized_ads) {
        this.non_personalized_ads = non_personalized_ads;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvents(Event event) {
        this.events.add(event);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
