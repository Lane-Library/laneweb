package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;

public class PagingEresourceList extends PagingList<Eresource> {

    private static final long serialVersionUID = 1L;

    private List<PagingLabel> pagingLabels;

    public PagingEresourceList(final Collection<Eresource> eresources, final PagingData pagingData) {
        super(eresources, pagingData);
    }

    public List<PagingLabel> getPagingLabels() {
        if (null != this.pagingLabels) {
            return this.pagingLabels;
        }
        this.pagingLabels = new LinkedList<PagingLabel>();
        PagingData pagingData = getPagingData();
        int pages = pagingData.getPages();
        int page = pagingData.getPage();
        int pageSize = pagingData.getPageSize();
        int size = this.size();
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
            this.pagingLabels.add(new PagingLabel(this.get(pageLabelStart).getTitle(), this.get(pageLabelEnd)
                    .getTitle(), numResults));
        }
        return this.pagingLabels;
    }
}
