package edu.stanford.irt.laneweb.folio;

import java.util.List;
import java.util.Map;

public interface UserService {

    public boolean addUser(final Map<String, Object> user);

    public List<Map<String, Object>> getUser(final String externalSystemId, final String email);
}
