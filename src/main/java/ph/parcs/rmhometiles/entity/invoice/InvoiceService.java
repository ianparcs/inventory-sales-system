package ph.parcs.rmhometiles.entity.invoice;

import javafx.collections.ObservableList;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductRepository;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.payment.Payment;
import ph.parcs.rmhometiles.util.PageUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        invoiceRepository.delete(invoice);
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoice.getId());
        return invoiceOptional.isEmpty();
    }

    @Override
    public Invoice saveEntity(Invoice invoice) {
        return invoiceRepository.save(saveCreatedBy(invoice));
    }

    public void saveOrderItem(Invoice invoice, ObservableList<OrderItem> items) {
        for (OrderItem item : items) {
            item.setInvoice(invoice);
        }
    }

    public String setInvoiceStatus(Money money) {
        if (money != null && money.isPositiveOrZero()) {
            return Payment.Status.PAID.name();
        }
        return Payment.Status.UNPAID.name();
    }

    public List<Invoice> findAllInvoice() {
        Iterable<Invoice> invoiceIterable = invoiceRepository.findAll();
        return Streamable.of(invoiceIterable).toList();
    }

    public List<Invoice> findAllInvoiceByDate(LocalDateTime[] dateTimeRange) {
        LocalDateTime startTime = dateTimeRange[0];
        LocalDateTime endTime = dateTimeRange[1];

        if (startTime == null) return invoiceRepository.findAll();
        return invoiceRepository.findAllByCreatedAtBetween(startTime, endTime);
    }

    public List<Invoice> filterByStatus(String status) {
        if (status.equalsIgnoreCase("All")) return findAllInvoice();
        return invoiceRepository.findAllByStatus(status);
    }

    @Override
    public Invoice createDefault() {
        Invoice invoice = new Invoice();
        invoice.setSubTotalAmount(Money.of(CurrencyUnit.of("PHP"), 0.00));
        invoice.setDiscountAmount(Money.of(CurrencyUnit.of("PHP"), 0.00));
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

