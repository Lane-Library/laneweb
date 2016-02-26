package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.solr.Eresource;

public class EresourceListPagingData extends PagingData {

    private static final long serialVersionUID = 1L;

    private String alpha;

    private ArrayList<PagingLabel> pagingLabels;

    public EresourceListPagingData(final List<Eresource> list, final int page, final String baseQuery,
            final String alpha) {
        super(list, page, baseQuery);
        this.alpha = alpha;
        this.pagingLabels = new ArrayList<>();
        int pages = getPages();
        int pageSize = getPageSize();
        int size = getSize();
        for (int i = 0; i < pages && page >= 0; i++) {
            int pageLabelStart;
            int pageLabelEnd;
            int numResults;
            if (i == 0) {
                pageLabelStart = 0;
                pageLabelEnd = pageSize - 1;
            } else {
                pageLabelStart = i * pageSize;
                pageLabelEnd = ((i + 1) * pageSize) - 1;
            }
            pageLabelEnd = pageLabelEnd >= size ? size - 1 : pageLabelEnd;
            numResults = (pageLabelEnd - pageLabelStart) + 1;
            this.pagingLabels.add(new PagingLabel(list.get(pageLabelStart).getTitle(),
                    list.get(pageLabelEnd).getTitle(), numResults));
        }
    }

    public String getAlpha() {
        return this.alpha;
    }

    public List<PagingLabel> getPagingLabels() {
        return this.pagingLabels;
    }
}
