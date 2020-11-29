package ph.parcs.rmhometiles.entity.invoice;

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
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InvoiceService extends BaseService<Invoice> {

    private InvoiceRepository invoiceRepository;
    private ProductRepository productRepository;

    @Override
    public Page<Invoice> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return invoiceRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public List<Invoice> findEntities(String query) {
        return invoiceRepository.findInvoiceByNameContains(query);
    }

    @Override
    public boolean deleteEntity(Invoice invoice) {
        return invoice == null;
    }

    @Override
    public Invoice saveEntity(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void updateLineItems(ObservableList<InvoiceLineItem> items) {
        for (InvoiceLineItem item : items) {
            if (item.getProduct() != null) {
                Product result = productRepository.findById(item.getProduct().getId()).orElse(null);
                item.setProduct(result);
            }
        }
        items.removeIf(lineItem -> lineItem.getProduct() == null);
    }

    public Invoice createDefault() {
        Invoice invoice = new Invoice();
        invoice.setName("");
        invoice.setId(0);
        return invoice;
    }

    @Autowired
    public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}

