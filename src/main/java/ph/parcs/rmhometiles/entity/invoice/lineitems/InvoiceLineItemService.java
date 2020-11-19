package ph.parcs.rmhometiles.entity.invoice.lineitems;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

import java.util.Set;

@Service
public class InvoiceLineItemService extends BaseService<InvoiceLineItem> {

    @Override
    public Page<InvoiceLineItem> findPages(int page, int itemPerPage, String name) {
        return null;
    }

    @Override
    public Set<InvoiceLineItem> findEntities(String query) {
        return null;
    }

    @Override
    public boolean deleteEntity(InvoiceLineItem item) {
        return false;
    }

    @Override
    public InvoiceLineItem saveEntity(InvoiceLineItem item) {
        return null;
    }


    @Override
    public InvoiceLineItem createDefault() {
        return null;
    }
}
