package ph.parcs.rmhometiles.supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SupplierService {

    private SupplierRepository supplierRepository;

    public ObservableList<Supplier> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(suppliers, ArrayList::new));
    }


    @Autowired
    public void setSupplierRepository(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
}
