package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface EntityRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    Page<T> findAllByNameContains(Pageable pageable, String name);

    List<T> findAllByNameContains(String name);


}
