package ph.parcs.rmhometiles.entity.invoice.lineitems;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseTableService;

import java.util.Set;

@Service
public class InvoiceLineItemService extends BaseTableService<InvoiceLineItem> {

    @Override
    public Page<InvoiceLineItem> findPages(int page, int itemPerPage, String name) {
        return null;
    }

    @Override
    public Set<InvoiceLineItem> findItems(String query) {
        return null;
    }

    @Override
    public boolean deleteRowItem(InvoiceLineItem item) {
        return false;
    }

    @Override
    public InvoiceLineItem saveRowItem(InvoiceLineItem item) {
        return null;
    }

    @Override
    public boolean isNew(InvoiceLineItem item) {
        return false;
    }

    @Override
    public InvoiceLineItem createDefault() {
        return null;
    }
}
