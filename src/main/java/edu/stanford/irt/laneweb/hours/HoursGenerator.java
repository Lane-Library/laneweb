package edu.stanford.irt.laneweb.hours;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.libraryhours.Hours;
import edu.stanford.irt.libraryhours.LibraryHoursService;

public class HoursGenerator extends AbstractGenerator {

    private static final int DAYS_PER_WEEK = 7;

    private static final long ONE_HOUR = Duration.ofHours(1).toMillis();

    private static final int WEEKS_TO_DISPLAY = 6;

    private SAXStrategy<List<List<Hours>>> saxStrategy;

    private LibraryHoursService service;

    private LocalDate today;

    private Validity validity;

    public HoursGenerator(final LibraryHoursService service, final SAXStrategy<List<List<Hours>>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
        this.validity = new ExpiresValidity(ONE_HOUR);
    }

    private static List<List<Hours>> divideIntoWeeks(final List<Hours> hours) {
        List<List<Hours>> lists = new ArrayList<>();
        int fromIndex = 0;
        int toIndex = DAYS_PER_WEEK;
        int numberOfWeeks = hours.size() / DAYS_PER_WEEK;
        for (int i = 0; i < numberOfWeeks; i++) {
            lists.add(hours.subList(fromIndex, toIndex));
            fromIndex += DAYS_PER_WEEK;
            toIndex += DAYS_PER_WEEK;
        }
        return lists;
    }

    @Override
    public Serializable getKey() {
        return this.today;
    }

    @Override
    public String getType() {
        return "hours";
    }

    @Override
    public Validity getValidity() {
        return this.validity;
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        this.today = ModelUtil.getObject(model, Model.TODAY, LocalDate.class);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        LocalDate monday = this.today.with(DayOfWeek.MONDAY);
        List<Hours> hours = this.service.getHours(monday, Period.ofWeeks(WEEKS_TO_DISPLAY));
        this.saxStrategy.toSAX(divideIntoWeeks(hours), xmlConsumer);
    }
}
