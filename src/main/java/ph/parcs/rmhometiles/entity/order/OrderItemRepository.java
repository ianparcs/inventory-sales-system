package ph.parcs.rmhometiles.entity.order;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.List;

@Repository
public interface OrderItemRepository extends EntityRepository<OrderItem, Integer> {

    @Override
    List<OrderItem> findAll();
}
