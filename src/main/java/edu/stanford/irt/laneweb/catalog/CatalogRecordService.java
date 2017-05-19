package edu.stanford.irt.laneweb.catalog;

import java.io.InputStream;
import java.util.List;

public interface CatalogRecordService {

    InputStream getRecords(List<String> params);
}
