package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import ph.parcs.rmhometiles.entity.BaseEntity;

@NoRepositoryBean
public interface ItemRepository<T extends BaseEntity, ID> extends PagingAndSortingRepository<T, ID> {
    Page<T> findAllByNameContains(Pageable pageable, String name);

}
