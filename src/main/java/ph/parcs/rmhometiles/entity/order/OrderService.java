package ph.parcs.rmhometiles.entity.order;

import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

@Service
public class OrderService extends BaseService<Orders> {

    @Override
    public Orders createDefault() {
        return new Orders();
    }
}
