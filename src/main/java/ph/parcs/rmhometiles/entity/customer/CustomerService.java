package ph.parcs.rmhometiles.entity.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CustomerService extends ItemService<Customer> {

    private CustomerRepository customerRepository;

    @Override
    public Page<Customer> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return customerRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public Set<Customer> findItems(String query) {
        return customerRepository.findCustomerByNameContains(query);
    }

    @Override
    public boolean deleteItem(Customer customer) {
        customerRepository.delete(customer);
        Optional<Customer> search = customerRepository.findById(customer.getId());
        return search.isEmpty();
    }

    @Override
    public Customer saveItem(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer createDefault() {
        Customer customer = new Customer();
        customer.setName("");
        customer.setId(0);
        return customer;
    }

    @Override
    public boolean isNew(Customer item) {
        return customerRepository.findById(item.getId()).isEmpty();
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
