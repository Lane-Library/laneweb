package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.CourseReservesService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CourseReservesItemListGenerator extends AbstractGenerator implements ModelAware {

    private CourseReservesService dao;

    private int id;

    private SAXStrategy<CourseReservesItemList> saxStrategy;

    public CourseReservesItemListGenerator(final CourseReservesService dao,
            final SAXStrategy<CourseReservesItemList> saxStrategy) {
        this.dao = dao;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        if (model.containsKey(Model.ID)) {
            this.id = Integer.parseInt(ModelUtil.getString(model, Model.ID));
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        CourseReservesItemList list;
        if (this.id == 0) {
            list = this.dao.getItems();
        } else {
            list = this.dao.getItems(this.id);
        }
        this.saxStrategy.toSAX(list, xmlConsumer);
    }
}
