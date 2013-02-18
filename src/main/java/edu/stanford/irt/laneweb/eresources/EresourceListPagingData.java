package edu.stanford.irt.laneweb.eresources;

import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.laneweb.resource.PagingData;

public class EresourceListPagingData extends PagingData {

    private String alpha;

    private PagingEresourceList list;

    private LinkedList<PagingLabel> pagingLabels;

    public EresourceListPagingData(final PagingEresourceList list, final int page, final String baseQuery,
            final String alpha) {
        super(list, page, baseQuery);
        this.list = list;
        this.alpha = alpha;
    }

    public String getAlpha() {
        return this.alpha;
    }

    public List<PagingLabel> getPagingLabels() {
        if (null != this.pagingLabels) {
            return this.pagingLabels;
        }
        this.pagingLabels = new LinkedList<PagingLabel>();
        int pages = getPages();
        int page = getPage();
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
            this.pagingLabels.add(new PagingLabel(this.list.get(pageLabelStart).getTitle(), this.list.get(pageLabelEnd)
                    .getTitle(), numResults));
        }
        return this.pagingLabels;
    }
}
