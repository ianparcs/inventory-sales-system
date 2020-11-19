package ph.parcs.rmhometiles.entity.supplier;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.List;
import java.util.Set;

@Repository
public interface SupplierRepository extends EntityRepository<Supplier, Integer> {

    @Override
    List<Supplier> findAll();

    Set<Supplier> findSupplierByNameContains(String query);

}
