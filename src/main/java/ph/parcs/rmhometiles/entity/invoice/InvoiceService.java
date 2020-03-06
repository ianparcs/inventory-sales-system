package ph.parcs.rmhometiles.entity.invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InvoiceService extends ItemService<Invoice> {

    private InvoiceRepository invoiceRepository;

    public ObservableList<Invoice> getInvoices() {
        List<Invoice> invoices = (List<Invoice>) invoiceRepository.findAll();
        invoices.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(invoices, ArrayList::new));
    }

    @Override
    public Page<Invoice> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return invoiceRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public Set<Invoice> findItems(String query) {
        return invoiceRepository.findInvoiceByNameContains(query);
    }

    @Override
    public boolean deleteItem(Invoice invoice) {
        return true;
    }

    @Override
    public Invoice saveItem(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice createDefault() {
        Invoice invoice = new Invoice();
        invoice.setName("");
        invoice.setId(0);
        return invoice;
    }

    @Override
    public boolean isNew(Invoice invoice) {
        return invoiceRepository.findById(invoice.getId()).isEmpty();
    }

    @Autowired
    public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
}

