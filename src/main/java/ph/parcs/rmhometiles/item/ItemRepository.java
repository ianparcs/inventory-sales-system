package ph.parcs.rmhometiles.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ItemRepository<T extends Item, ID> extends PagingAndSortingRepository<T, ID> {
    Page<T> findAllByNameContains(Pageable pageable, String name);

}
