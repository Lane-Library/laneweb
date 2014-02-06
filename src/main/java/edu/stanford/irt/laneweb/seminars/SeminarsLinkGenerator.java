package edu.stanford.irt.laneweb.seminars;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class SeminarsLinkGenerator extends AbstractGenerator implements ParametersAware {

    private static final MessageFormat URL_FORMAT = new MessageFormat(
            "http://med.stanford.edu/seminars/validatecmecalendar.do?filter=true&selMonth={0}&selDay={1}&selYear={2}&futureNumberDays={3}&departmentId=0&seminarLocation=0&keyword=&courseType={4}");

    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

    private String days;

    private final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");

    private SAXStrategy<Map<String, String>> saxStrategy;

    private String type;

    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    public SeminarsLinkGenerator(final SAXStrategy<Map<String, String>> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Date today = new Date();
        String day;
        String month;
        String year;
        synchronized (this) {
            day = this.dayFormat.format(today);
            month = this.monthFormat.format(today);
            year = this.yearFormat.format(today);
        }
        Object[] urlParams = { month, day, year, this.days, this.type };
        String url = URL_FORMAT.format(urlParams);
        Map<String, String> seminarMap = new HashMap<String, String>();
        seminarMap.put("type", this.type);
        seminarMap.put("days", this.days);
        seminarMap.put("day", day);
        seminarMap.put("month", month);
        seminarMap.put("year", year);
        seminarMap.put("url", url);
        this.saxStrategy.toSAX(seminarMap, xmlConsumer);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        this.days = parameters.get("days");
        this.type = parameters.get(Model.TYPE);
    }
}
