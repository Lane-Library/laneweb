package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CourseReservesItemListGenerator extends AbstractGenerator {

    private String id;

    private SAXStrategy<CourseReservesItemList> saxStrategy;

    private CourseReservesService service;

    public CourseReservesItemListGenerator(final CourseReservesService service,
            final SAXStrategy<CourseReservesItemList> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        if (model.containsKey(Model.ID)) {
            this.id = ModelUtil.getString(model, Model.ID);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        CourseReservesItemList list;
        if (null == this.id || this.id.isBlank()) {
            list = this.service.getItems();
        } else {
            list = this.service.getItems(this.id);
        }
        this.saxStrategy.toSAX(list, xmlConsumer);
    }
}
