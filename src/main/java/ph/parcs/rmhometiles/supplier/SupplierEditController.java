package ph.parcs.rmhometiles.supplier;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.Supplier;
import ph.parcs.rmhometiles.item.EditItemController;

@Controller
public class SupplierEditController extends EditItemController<Supplier> {

    @FXML
    private JFXTextField tfContact;
    @FXML
    private JFXTextField tfAddress;
    @FXML
    private JFXTextField tfName;

    @Override
    protected Supplier unbindFields(Integer id) {
        Supplier supplier = new Supplier();
        supplier.setId(id);

        if (!StringUtils.isEmpty(tfName.getText())) supplier.setName(tfName.getText());
        if (!StringUtils.isEmpty(tfContact.getText())) supplier.setContact(tfContact.getText());
        if (!StringUtils.isEmpty(tfAddress.getText())) supplier.setAddress(tfAddress.getText());

        return supplier;
    }

    @Override
    protected void bindFields(Supplier supplier) {
        if (!StringUtils.isEmpty(supplier.getName())) tfName.setText(supplier.getName());
        if (!StringUtils.isEmpty(supplier.getContact())) tfContact.setText(supplier.getContact());
        if (!StringUtils.isEmpty(supplier.getAddress())) tfAddress.setText(supplier.getAddress());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
        tfContact.clear();
        tfAddress.clear();
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.itemService = supplierService;
    }

}
