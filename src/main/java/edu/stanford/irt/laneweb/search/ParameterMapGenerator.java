package edu.stanford.irt.laneweb.search;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class ParameterMapGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private Map<String, Object> model;

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        /* FIXME: Adding elements of an entry set may fail due to reuse of Entry
         * objectsfindbugs : DMI_ENTRY_SETS_MAY_REUSE_ENTRY_OBJECTS The
         * entrySet() method is allowed to return a view of the underlying Map
         * in which a single Entry object is reused and returned during the
         * iteration. As of Java 1.6, both IdentityHashMap and EnumMap did so.
         * When iterating through such a Map, the Entry value is only valid
         * until you advance to the next iteration. If, for example, you try to
         * pass such an entrySet to an addAll method, things will go badly
         * wrong.
         */
        Set<Entry<String, String[]>> entries = new HashSet<Entry<String, String[]>>();
        entries.addAll(ModelUtil.getObject(this.model, Model.PARAMETER_MAP, Map.class).entrySet());
        marshall(entries, xmlConsumer);
    }
}
