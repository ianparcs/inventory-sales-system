package ph.parcs.rmhometiles.entity.invoice.lineitems;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.order.OrderItem;

import java.util.List;

@Service
public class InvoiceLineItemService extends BaseService<OrderItem> {

    @Override
    public Page<OrderItem> findPages(int page, int itemPerPage, String name) {
        return null;
    }

    @Override
    public List<OrderItem> findEntities(String query) {
        return null;
    }

    @Override
    public boolean deleteEntity(OrderItem item) {
        return false;
    }

    @Override
    public OrderItem saveEntity(OrderItem item) {
        return null;
    }


    @Override
    public OrderItem createDefault() {
        return null;
    }
}
