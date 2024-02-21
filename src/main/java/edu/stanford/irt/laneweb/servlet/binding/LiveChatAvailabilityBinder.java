package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.livechat.LiveChatAvailabilityService;
import edu.stanford.irt.laneweb.model.Model;

public class LiveChatAvailabilityBinder implements DataBinder {

    private LiveChatAvailabilityService service;

    public LiveChatAvailabilityBinder(final LiveChatAvailabilityService service) {
        this.service = service;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.LIVE_CHAT_AVAILABLE, Boolean.valueOf(this.service.isAvailable()));
    }
}
