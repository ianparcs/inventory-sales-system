package ph.parcs.rmhometiles.entity.order;


import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

@Repository
public interface OrdersRepository extends EntityRepository<Orders, Integer> {
}
