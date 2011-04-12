package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.livechat.Schedule;
import edu.stanford.irt.laneweb.model.Model;


public class LiveChatScheduleBinder implements DataBinder {
    
    private Schedule schedule = new Schedule();

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.LIVE_CHAT_AVAILABLE, Boolean.valueOf(this.schedule.isAvailable()));
    }
}
