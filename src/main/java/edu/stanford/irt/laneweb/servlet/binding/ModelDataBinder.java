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

    private JsonMapper objectMapper;

    public ModelDataBinder(final Set<String> keys, final JsonMapper objectMapper) {
        this.keys = new HashSet<>(keys);
        this.objectMapper = objectMapper;
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
            JsonGenerator jsonGenerator = this.objectMapper.createGenerator(stringWriter);
            this.objectMapper.writeValue(jsonGenerator, jsonModel);
            model.put(Model.MODEL, stringWriter.toString());
        } catch (JacksonException e) {
            throw new LanewebException(e);
        }
    }
}
