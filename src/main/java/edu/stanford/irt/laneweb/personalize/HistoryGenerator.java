package edu.stanford.irt.laneweb.personalize;

import java.util.List;

import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class HistoryGenerator extends AbstractMarshallingGenerator {

    private List<History> history;

    public void generate() {
        marshall(this.history);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.history = ModelUtil.getObject(this.model, Model.HISTORY, List.class);
    }
}
