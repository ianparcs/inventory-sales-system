package ph.parcs.rmhometiles.entity.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class OrderItemService extends BaseService<OrderItem> {

    public OrderItem createDefault() {
        OrderItem customer = new OrderItem();
        customer.setName("");
        customer.setId(0);
        return customer;
    }

    @Autowired
    public void setItemRepository(OrderItemRepository orderItemRepository) {
        this.entityRepository = orderItemRepository;
    }

}
