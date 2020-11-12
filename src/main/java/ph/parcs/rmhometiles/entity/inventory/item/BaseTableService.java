package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public abstract class BaseTableService<T extends BaseEntity> {

    public boolean isEmpty(T item) {
        return item == null;
    }

    public abstract Page<T> findPages(int page, int itemPerPage, String name);

    public abstract Set<T> findItems(String query);

    public abstract boolean deleteRowItem(T item);

    public abstract T saveRowItem(T item);

    public abstract boolean isNew(T item);

    public abstract T createDefault();

}
