package ph.parcs.rmhometiles.entity.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CustomerService extends BaseService<Customer> {

    private CustomerRepository customerRepository;

    @Override
    public Customer createDefault() {
        Customer customer = new Customer();
        customer.setName("");
        customer.setId(0);
        return customer;
    }

    @Autowired
    public void setItemRepository(CustomerRepository customerRepository) {
        this.entityRepository = customerRepository;
        this.customerRepository = (CustomerRepository) entityRepository;
    }

}
