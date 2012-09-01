package edu.stanford.irt.laneweb.servlet.binding;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;

public class ModelDataBinder implements DataBinder {

    private static final class IPGroupSerializer extends JsonSerializer<IPGroup> {

        @Override
        public Class<IPGroup> handledType() {
            return IPGroup.class;
        }

        @Override
        public void serialize(final IPGroup value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
            jgen.writeString(value.toString());
        }
    }

    private static final class TicketSerializer extends JsonSerializer<Ticket> {

        @Override
        public Class<Ticket> handledType() {
            return Ticket.class;
        }

        @Override
        public void serialize(final Ticket value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
            jgen.writeString(value.toString());
        }
    }

    private Set<String> keys;

    private ObjectMapper objectMapper;

    public ModelDataBinder(final Set<String> keys) {
        this.keys = keys;
        this.objectMapper = new ObjectMapper();
        //add serializers for IPGroup and Ticket:
        SimpleModule module = new SimpleModule("ipgroup", new Version(1, 0, 0, null));
        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        this.objectMapper.registerModule(module);
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Map<String, Object> modelModel = new HashMap<String, Object>();
        for (Entry<String, Object> entry : model.entrySet()) {
            String key = entry.getKey();
            if (this.keys.contains(key)) {
                modelModel.put(key, entry.getValue());
            }
        }
        StringWriter stringWriter = new StringWriter();
        try {
            JsonGenerator jsonGenerator = this.objectMapper.getJsonFactory().createJsonGenerator(stringWriter);
            this.objectMapper.writeValue(jsonGenerator, modelModel);
            model.put(Model.MODEL, stringWriter.toString());
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
