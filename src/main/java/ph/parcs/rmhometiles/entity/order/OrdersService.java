package ph.parcs.rmhometiles.entity.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class OrdersService extends BaseService<Orders> {

    @Override
    public Orders createDefault() {
        return new Orders();
    }

    Orders createOrder(Customer customer) {
        Orders orders = new Orders();
        orders.setUpdatedAt(LocalDateTime.now());
        orders.setCreatedAt(LocalDateTime.now());
        orders.setCustomer(customer);
        return orders;
    }

    @Autowired
    public void setItemRepository(OrdersRepository ordersRepository) {
        this.entityRepository = ordersRepository;
    }

}
