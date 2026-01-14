package edu.stanford.irt.laneweb.servlet.binding;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.json.JsonMapper;

public class ModelDataBinder implements DataBinder {

    private Set<String> keys;

    private JsonMapper jsonMapper;

    public ModelDataBinder(final Set<String> keys, final JsonMapper jsonMapper) {
        this.keys = new HashSet<>(keys);
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Map<String, Object> jsonModel = new HashMap<>();
        for (Entry<String, Object> entry : model.entrySet()) {
            String key = entry.getKey();
            if (this.keys.contains(key)) {
                jsonModel.put(key, entry.getValue());
            }
        }
        StringWriter stringWriter = new StringWriter();
        try {
            JsonGenerator jsonGenerator = this.jsonMapper.createGenerator(stringWriter);
            this.jsonMapper.writeValue(jsonGenerator, jsonModel);
            model.put(Model.MODEL, stringWriter.toString());
        } catch (JacksonException e) {
            throw new LanewebException(e);
        }
    }
}
