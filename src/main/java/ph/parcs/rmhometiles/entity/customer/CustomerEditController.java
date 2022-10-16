package ph.parcs.rmhometiles.entity.customer;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;

@Controller
public class CustomerEditController extends EditItemController<Customer> {

    @FXML
    private JFXTextField tfContact;
    @FXML
    private JFXTextField tfAddress;
    @FXML
    private JFXTextField tfName;

    @Override
    protected Customer createEntity(Integer id) {
        Customer customer = new Customer();
        customer.setId(id);

        if (!StringUtils.isEmpty(tfContact.getText())) customer.setContact(tfContact.getText());
        if (!StringUtils.isEmpty(tfName.getText())) customer.setName(tfName.getText());
        if (!StringUtils.isEmpty(tfAddress.getText())) customer.setAddress(tfAddress.getText());
        return customer;
    }

    @Override
    protected void bindFields(Customer customer) {
        if (!StringUtils.isEmpty(customer.getName())) tfName.setText(customer.getName());
        if (!StringUtils.isEmpty(customer.getContact())) tfContact.setText(customer.getContact());
        if (!StringUtils.isEmpty(customer.getAddress())) tfAddress.setText(customer.getAddress());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
        tfContact.clear();
        tfAddress.clear();
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.baseService = customerService;
    }

}
