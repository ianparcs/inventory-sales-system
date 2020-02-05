package ph.parcs.rmhometiles.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.product.Product;

@Service
public abstract class ItemService<T extends Item> {

    protected abstract Page<T> findPages(int page, int itemPerPage, String name);

    protected PageRequest requestPage(int page, int itemPerPage) {
        return PageRequest.of(page, itemPerPage);
    }

    public boolean isItemEmpty(Product product) {
        return product == null;
    }

    public abstract boolean deleteItem(T item);

    public abstract T saveItem(T item);
}
