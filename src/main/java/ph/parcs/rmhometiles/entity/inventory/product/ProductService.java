package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.file.FileImage;
import ph.parcs.rmhometiles.util.FileUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService extends ItemService<Product> {

    private ProductRepository productRepository;
    private FileService fileService;

    @Override
    public Page<Product> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return productRepository.findAllByCodeContains(pageRequest, name);
    }

    public Set<Product> findItems(String query) {
        StringProperty queryProp = new SimpleStringProperty(query);
        return productRepository.findProductByCodeContains(queryProp);
    }

    @Override
    public boolean deleteItem(Product product) {
        FileImage fileImage = product.getFileImage();
        if (fileImage != null && !StringUtils.isEmpty(fileImage.getPath())) {
            fileService.deleteFile(fileImage.getName());
        }

        productRepository.delete(product);

        Optional<Product> productOptional = productRepository.findById(product.getId());
        return productOptional.isEmpty();
    }

    @Override
    @SneakyThrows
    public Product saveItem(Product product) {
        FileImage fileImage = product.getFileImage();
        if (fileImage != null && !StringUtils.isEmpty(fileImage.getPath())) {
            saveFile(fileImage);
        }
        return productRepository.save(product);
    }

    private void saveFile(FileImage fileImage) {
        String fileName = FileUtils.getFileName(fileImage.getPath());
        String des = FileUtils.getTargetPath(fileName);
        String src = fileImage.getPath();
        fileImage.setName(fileName);
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
