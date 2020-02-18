package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.BaseEntity;

@Service
public abstract class ItemService<T extends BaseEntity> {

    public ItemPageEntry getPageEntries(Page<T> items) {
        long toEntry = items.getNumberOfElements() * (items.getNumber() + 1);
        long fromEntry = (toEntry - items.getNumberOfElements()) + 1;

        if (items.isLast()) {
            toEntry = items.getTotalElements();
            fromEntry = toEntry - items.getNumberOfElements() + 1;
        } else if (items.isFirst()) {
            fromEntry = 1;
            if (items.getTotalElements() == 0) fromEntry = 0;
        }
        return new ItemPageEntry(toEntry, fromEntry);
    }

    public PageRequest requestPage(int page, int itemPerPage) {
        return PageRequest.of(page, itemPerPage);
    }

    public boolean isEmpty(T item) {
        return item == null;
    }

    public abstract Page<T> findPages(int page, int itemPerPage, String name);

    public abstract boolean deleteItem(T item);

    public abstract T saveItem(T item);

    public abstract boolean isNew(T item);

    public abstract T createDefault();

}
