package ph.parcs.rmhometiles.customer;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.Customer;
import ph.parcs.rmhometiles.item.EditItemController;

@Controller
public class CustomerEditController extends EditItemController<Customer> {

    @FXML
    private JFXTextField tfContact;
    @FXML
    private JFXTextField tfAddress;
    @FXML
    private JFXTextField tfName;

    @Override
    protected Customer unbindFields(Integer id) {
        Customer customer = new Customer();
        customer.setAddress(tfAddress.getText());
        customer.setContact(tfContact.getText());
        customer.setName(tfName.getText());
        customer.setId(id);
        return customer;
    }

    @Override
    protected void bindFields(Customer customer) {
        tfName.setText(customer.getName());
        tfContact.setText(customer.getContact());
        tfAddress.setText(customer.getAddress());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.itemService = customerService;
    }

}
