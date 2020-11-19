package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface BaseServiceInterface<T, ID> {

    Page<T> findPages(int page, int itemPerPage, String name);

    Set<T> findEntities(String query);

    T findEntityById(ID id);

    T saveEntity(T item);

    T createDefault();

    boolean isExist(Integer id);

    boolean deleteEntity(T item);
}
