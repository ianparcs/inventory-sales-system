package ph.parcs.rmhometiles.entity.supplier;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.List;
import java.util.Set;

@Repository
public interface SupplierRepository extends ItemRepository<Supplier, Integer> {

    @Override
    List<Supplier> findAll();

    Set<Supplier> findSupplierByNameContains(String query);

}
