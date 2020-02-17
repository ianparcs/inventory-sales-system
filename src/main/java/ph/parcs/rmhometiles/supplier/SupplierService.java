package ph.parcs.rmhometiles.supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.Product;
import ph.parcs.rmhometiles.entity.Supplier;
import ph.parcs.rmhometiles.inventory.ProductRepository;
import ph.parcs.rmhometiles.item.ItemService;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SupplierService extends ItemService<Supplier> {

    private SupplierRepository supplierRepository;
    private ProductRepository productRepository;

    public ObservableList<Supplier> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        suppliers.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(suppliers, ArrayList::new));
    }

    @Override
    public Page<Supplier> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return supplierRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public boolean deleteItem(Supplier supplier) {
        Supplier clearProd = removeProductsOfSupplier(supplier);
        supplierRepository.delete(clearProd);
        Optional<Supplier> search = supplierRepository.findById(supplier.getId());
        return search.isEmpty();
    }

    private Supplier removeProductsOfSupplier(Supplier supplier) {
        Set<Product> productSet = productRepository.findProductsBySupplier(supplier);
        if (productSet != null) {
            for (Product product : productSet) {
                product.setSupplier(null);
            }
        }
        supplier.setProducts(null);
        return supplier;
    }

    @Override
    public Supplier saveItem(Supplier item) {
        return supplierRepository.save(item);
    }

    @Override
    public Supplier createDefault() {
        Supplier supplier = new Supplier();
        supplier.setName("");
        supplier.setId(0);
        return supplier;
    }

    @Override
    public boolean isNew(Supplier item) {
        return supplierRepository.findById(item.getId()).isEmpty();
    }

    public Optional<Supplier> findSupplierByProduct(ObservableList<Supplier> items, Product product) {
        Optional<Supplier> search = Optional.empty();
        if (product.getSupplier() != null) {
            search = items.stream()
                    .filter(item -> item.getId().equals(product.getSupplier().getId()))
                    .findAny();
        }
        return search;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setSupplierRepository(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
}
