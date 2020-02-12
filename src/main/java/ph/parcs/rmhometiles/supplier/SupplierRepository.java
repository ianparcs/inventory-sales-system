package ph.parcs.rmhometiles.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.item.ItemRepository;

import java.util.List;

@Repository
public interface SupplierRepository extends ItemRepository<Supplier, Integer> {

    @Override
    List<Supplier> findAll();
}
