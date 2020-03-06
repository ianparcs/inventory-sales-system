package ph.parcs.rmhometiles.entity.inventory.product;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;
import ph.parcs.rmhometiles.file.FileService;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService extends ItemService<Product> {

    private ProductRepository productRepository;
    private FileService fileService;

    @Override
    public Page<Product> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return productRepository.findAllByNameContains(pageRequest, name);
    }

    public Set<Product> findItems(String query) {
        return productRepository.findProductByNameContains(query);
    }

    @Override
    public boolean deleteItem(Product product) {
        productRepository.delete(product);
        String filename = product.getFileName();
        if (!StringUtils.isEmpty(filename)) {
            fileService.deleteFile(filename);
        }
        Optional<Product> productOptional = productRepository.findById(product.getId());
        return productOptional.isEmpty();
    }

    @Override
    @SneakyThrows
    public Product saveItem(Product product) {
        product.setId(productRepository.save(product).getId());

        if (!product.getFilePath().isEmpty()) {
            saveProductImage(product);
        }

        return productRepository.save(product);
    }

    private void saveProductImage(Product product) {
        String fileName = fileService.getNewName(product.getId().toString(), product.getFilePath());
        String des = fileService.getFullTargetPath(fileName);
        String src = product.getFilePath();
        product.setFileName(fileName);
        fileService.saveImage(src, des);
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
