package ph.parcs.rmhometiles.supplier;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.item.EditItemController;

@Controller
public class SupplierEditController extends EditItemController<Supplier> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected Supplier unbindFields(Integer id) {
        Supplier supplier = new Supplier();
        supplier.setName(tfName.getText());
        supplier.setId(id);
        return supplier;
    }

    @Override
    protected void bindFields(Supplier supplier) {
        tfName.setText(supplier.getName());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.itemService = supplierService;
    }

}
