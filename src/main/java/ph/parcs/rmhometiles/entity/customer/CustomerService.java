package ph.parcs.rmhometiles.entity.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CustomerService extends ItemService<Customer> {

    private CustomerRepository customerRepository;

    public ObservableList<Customer> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        customers.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(customers, ArrayList::new));
    }

    @Override
    public Page<Customer> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return customerRepository.findAllByNameContains(pageRequest, name);
    }

    public List<Customer> findCustomer(String name) {
        return customerRepository.findCustomerByNameContains(name);
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
