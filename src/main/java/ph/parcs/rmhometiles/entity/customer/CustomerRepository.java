package ph.parcs.rmhometiles.entity.customer;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.List;

@Repository
public interface CustomerRepository extends ItemRepository<Customer, Integer> {

    @Override
    List<Customer> findAll();

    List<Customer> findCustomerByNameContains(String name);
}
