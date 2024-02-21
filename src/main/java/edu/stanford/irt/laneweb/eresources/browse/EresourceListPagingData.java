package edu.stanford.irt.laneweb.eresources.browse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.resource.PagingData;

public class EresourceListPagingData extends PagingData {

    private static final long serialVersionUID = 1L;

    private String alpha;

    private transient List<Eresource> list;

    private int page;

    private List<PagingLabel> pagingLabels;

    public EresourceListPagingData(final List<Eresource> list, final int page, final String baseQuery,
            final String alpha) {
        super(list, page, baseQuery);
        this.list = new ArrayList<>(list);
        this.page = page;
        this.alpha = alpha;
    }

    public String getAlpha() {
        return this.alpha;
    }

    public List<PagingLabel> getPagingLabels() {
        if (this.pagingLabels == null) {
            List<PagingLabel> labels = new ArrayList<>();
            int pages = getPages();
            int pageSize = getPageSize();
            int size = getSize();
            for (int i = 0; i < pages && this.page >= 0; i++) {
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
                labels.add(new PagingLabel(this.list.get(pageLabelStart).getTitle(),
                        this.list.get(pageLabelEnd).getTitle(), numResults));
            }
            this.pagingLabels = Collections.unmodifiableList(labels);
        }
        return this.pagingLabels;
    }
}
