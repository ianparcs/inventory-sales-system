package ph.parcs.rmhometiles.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.item.ItemService;

import java.util.Optional;

@Service
public class ProductService extends ItemService<Product> {

    private ProductRepository productRepository;

    @Override
    public Page<Product> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return productRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public boolean deleteItem(Product item) {
        productRepository.delete(item);
        Optional<Product> product = productRepository.findById(item.getId());
        return product.isEmpty();
    }

    @Override
    public Product saveItem(Product item) {
        return productRepository.save(item);
    }

    @Override
    public boolean isNew(Product item) {
        return productRepository.findById(item.getId()).isEmpty();
    }

    @Override
    public Product createDefault() {
        return new Product();
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
