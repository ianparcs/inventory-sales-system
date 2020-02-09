package ph.parcs.rmhometiles.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public abstract class ItemService<T extends Item> {

    public PageRequest requestPage(int page, int itemPerPage) {
        return PageRequest.of(page, itemPerPage);
    }

    public boolean isEmpty(T item) {
        return item == null;
    }

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

    public abstract Page<T> findPages(int page, int itemPerPage, String name);

    public abstract boolean deleteItem(T item);

    public abstract T saveItem(T item);

    public abstract boolean isNew(T item);
}
