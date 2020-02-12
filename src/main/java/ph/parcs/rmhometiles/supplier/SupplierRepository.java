package ph.parcs.rmhometiles.supplier;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.item.ItemRepository;

import java.util.List;

@Repository
public interface SupplierRepository extends ItemRepository<Supplier, Integer> {

    @Override
    List<Supplier> findAll();
}
