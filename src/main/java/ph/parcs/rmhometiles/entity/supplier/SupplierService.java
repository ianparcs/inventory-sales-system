package ph.parcs.rmhometiles.entity.supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductRepository;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SupplierService extends BaseService<Supplier> {

    private SupplierRepository supplierRepository;
    private ProductRepository productRepository;

    public ObservableList<Supplier> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(suppliers, ArrayList::new));
    }

    @Override
    public Page<Supplier> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return supplierRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public List<Supplier> findEntities(String query) {
        return supplierRepository.findSupplierByNameContains(query);
    }

    @Override
    public boolean deleteEntity(Supplier supplier) {
        Supplier clearProd = removeProductsOfSupplier(supplier);
        supplierRepository.delete(clearProd);
        Optional<Supplier> search = supplierRepository.findById(supplier.getId());
        return search.isEmpty();
    }

    private Supplier removeProductsOfSupplier(Supplier supplier) {
        List<Product> productSet = productRepository.findProductsBySupplier(supplier);
        if (productSet != null) {
            for (Product product : productSet) {
                product.setSupplier(null);
            }
        }
        supplier.setProducts(null);
        return supplier;
    }

    @Override
    public Supplier saveEntity(Supplier item) {
        return supplierRepository.save(item);
    }

    @Override
    public Supplier createDefault() {
        Supplier supplier = new Supplier();
        supplier.setName("");
        supplier.setId(0);
        return supplier;
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
