package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

@NoRepositoryBean
public interface EntityRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    Page<T> findAllByNameContains(Pageable pageable, String name);

    Set<T> findAllByNameContains(String name);


}
