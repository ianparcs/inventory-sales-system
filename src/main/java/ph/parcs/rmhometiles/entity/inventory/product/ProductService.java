package ph.parcs.rmhometiles.entity.inventory.product;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.inventory.stock.StockService;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.order.OrderItemService;
import ph.parcs.rmhometiles.exception.AppException;
import ph.parcs.rmhometiles.exception.ErrorCode;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService extends BaseService<Product> {
    private ProductRepository productRepository;
    private OrderItemService orderItemService;
    private StockService stockService;
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
    public boolean deleteEntity(Product product) throws AppException {
        ImageProduct imageProduct = product.getImageProduct();
        if (imageProduct != null && !StringUtils.isEmpty(imageProduct.getPath())) {
            fileService.deleteFile(imageProduct.getName());
        }

        if (orderItemService.isItemsExists(product.getOrderItems())) {
            throw new AppException(ErrorCode.ITEM_LOCKED);
        }

        productRepository.delete(product);

        Optional<Product> productOptional = productRepository.findById(product.getId());
        return productOptional.isEmpty();
    }

    public void updateProductStocks(Set<OrderItem> items) {
        for (OrderItem item : items) {
            Stock stock = stockService.computeStocks(item.getProduct(), item.getQuantity());
            Product product = item.getProduct();
            product.setStock(stock);
            saveEntity(product);
        }
    }

    @Override
    @SneakyThrows
    public Product saveEntity(Product product) {
        ImageProduct imageProduct = product.getImageProduct();
        if (imageProduct != null && !StringUtils.isEmpty(imageProduct.getPath())) {
            fileService.saveImage(imageProduct);
        }
        return productRepository.save(saveCreatedBy(product));
    }

    @Override
    public Product createDefault() {
        return new Product();
    }

    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
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

    @Autowired
    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

}
