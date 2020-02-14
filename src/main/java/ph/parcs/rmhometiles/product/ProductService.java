package ph.parcs.rmhometiles.product;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.Product;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.item.ItemService;

import java.util.Optional;

@Service
public class ProductService extends ItemService<Product> {

    private ProductRepository productRepository;
    private FileService fileService;

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
    @SneakyThrows
    public Product saveItem(Product product) {
        Product saveProduct = productRepository.save(product);

        String imagePath = product.getImagePath();
        String imageName = saveProduct.getId().toString();

        if (!imagePath.isEmpty()) {
            String imageDes = fileService.generateNewFilePath(imagePath, imageName);
            saveProduct.setImagePath(imageDes);
            productRepository.save(saveProduct);
        }
        return saveProduct;
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

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
