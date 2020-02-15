package ph.parcs.rmhometiles.customer;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.Customer;
import ph.parcs.rmhometiles.item.ItemRepository;

import java.util.List;

@Repository
public interface CustomerRepository extends ItemRepository<Customer, Integer> {

    @Override
    List<Customer> findAll();
}
