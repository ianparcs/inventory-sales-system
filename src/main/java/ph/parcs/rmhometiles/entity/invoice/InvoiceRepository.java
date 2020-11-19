package ph.parcs.rmhometiles.entity.invoice;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.Set;

@Repository
public interface InvoiceRepository extends EntityRepository<Invoice, Integer> {

    Set<Invoice> findInvoiceByNameContains(String query);
}
