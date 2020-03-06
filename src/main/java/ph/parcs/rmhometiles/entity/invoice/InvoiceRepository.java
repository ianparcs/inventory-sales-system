package ph.parcs.rmhometiles.entity.invoice;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.Set;

@Repository
public interface InvoiceRepository extends ItemRepository<Invoice, Integer> {

    Set<Invoice> findInvoiceByNameContains(String query);
}
