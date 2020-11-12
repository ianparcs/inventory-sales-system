package ph.parcs.rmhometiles.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.ItemPageEntry;

public class PageUtil {

    public static ItemPageEntry getPageEntries(Page<BaseEntity> items) {
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

    public static PageRequest requestPage(int page, int itemPerPage) {
        return PageRequest.of(page, itemPerPage);
    }

}
