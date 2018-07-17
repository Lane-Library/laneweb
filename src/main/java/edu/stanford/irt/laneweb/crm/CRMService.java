package edu.stanford.irt.laneweb.crm;

import java.io.IOException;
import java.util.Map;

public interface CRMService {

    int submitRequest(Map<String, Object> feedback) throws IOException;
}
