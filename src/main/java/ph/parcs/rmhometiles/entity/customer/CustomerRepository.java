package ph.parcs.rmhometiles.entity.customer;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.List;

@Repository
public interface CustomerRepository extends EntityRepository<Customer, Integer> {

    @Override
    List<Customer> findAll();
}
