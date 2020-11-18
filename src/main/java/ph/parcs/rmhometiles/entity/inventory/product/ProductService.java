package ph.parcs.rmhometiles.entity.inventory.product;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.BaseTableService;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.util.FileUtils;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService extends BaseTableService<Product> {

    private ProductRepository productRepository;
    private FileService fileService;

    @Override
    public Page<Product> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return productRepository.findAllByCodePropertyContains(pageRequest, name);
    }

    public Set<Product> findItems(String query) {
        return productRepository.findAllByCodePropertyContains(query);
    }

    @Override
    public boolean deleteRowItem(Product product) {
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
    public Product saveRowItem(Product product) {
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
