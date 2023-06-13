package edu.stanford.irt.laneweb.servlet.binding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;


public class TodayDataBinder implements DataBinder {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    @Override
    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.TODAY, LocalDate.now(AMERICA_LA));
    }
}
