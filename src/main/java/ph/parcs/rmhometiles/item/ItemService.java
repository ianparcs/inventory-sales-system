package ph.parcs.rmhometiles.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public abstract class ItemService<T extends Item> {

    public PageRequest requestPage(int page, int itemPerPage) {
        return PageRequest.of(page, itemPerPage);
    }

    public boolean isItemEmpty(T item) {
        return item == null;
    }

    public abstract Page<T> findPages(int page, int itemPerPage, String name);

    public abstract boolean deleteItem(T item);

    public abstract T saveItem(T item);
}
