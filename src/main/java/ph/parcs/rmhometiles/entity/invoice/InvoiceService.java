package ph.parcs.rmhometiles.entity.invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InvoiceService extends BaseService<Invoice> {

    private InvoiceRepository invoiceRepository;

    public ObservableList<Invoice> getInvoices() {
        List<Invoice> invoices = (List<Invoice>) invoiceRepository.findAll();
        invoices.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(invoices, ArrayList::new));
    }

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
        return true;
    }

    @Override
    public Invoice saveEntity(Invoice invoice) {
        return invoiceRepository.save(invoice);
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
}

