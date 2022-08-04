package ph.parcs.rmhometiles.entity.payment;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

@Repository
public interface PaymentRepository extends EntityRepository<Payment, Integer> {

}
