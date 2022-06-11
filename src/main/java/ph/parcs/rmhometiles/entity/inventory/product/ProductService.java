package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.collections.ObservableList;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.util.FileUtils;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService extends BaseService<Product> {

    private ProductRepository productRepository;
    private FileService fileService;


    @Override
    public Page<Product> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return productRepository.findAllByCodeContains(pageRequest, name);
    }

    public List<Product> findEntities(String query) {
        return productRepository.findAllByCodeContains(query);
    }

    @Override
    public boolean deleteEntity(Product product) {
        ImageProduct imageProduct = product.getImageProduct();
        if (imageProduct != null && !StringUtils.isEmpty(imageProduct.getPath())) {
            fileService.deleteFile(imageProduct.getName());
        }

        productRepository.delete(product);

        Optional<Product> productOptional = productRepository.findById(product.getId());
        return productOptional.isEmpty();
    }


    @Override
    @SneakyThrows
    public Product saveEntity(Product product) {
        ImageProduct imageProduct = product.getImageProduct();
        if (imageProduct != null && !StringUtils.isEmpty(imageProduct.getPath())) {
            saveFile(imageProduct);
        }
        return productRepository.save(product);
    }

    private void saveFile(ImageProduct imageProduct) {
        String fileName = FileUtils.getFileName(imageProduct.getPath());
        String des = FileUtils.getTargetPath(fileName);
        String src = imageProduct.getPath();
        imageProduct.setName(fileName);
        fileService.saveImage(src, des);
    }

    @Override
    public Product createDefault() {
        return new Product();
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setItemRepository(ProductRepository productRepository) {
        this.entityRepository = productRepository;
        this.productRepository = (ProductRepository) entityRepository;
    }

    public OrderItem checkQuantity(ObservableList<OrderItem> items) {
        OrderItem temp = null;
        for (OrderItem item : items) {
            temp = item;
            if (temp != null) {
                int quantity = temp.getQuantity();
                if (quantity <= 0) return item;
            }
        }
        return null;
    }
}
