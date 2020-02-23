package ph.parcs.rmhometiles.entity.base;

import javafx.collections.ObservableList;
import org.springframework.data.domain.Page;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import java.util.Optional;

public class BaseEntityService extends ItemService<BaseEntity> {

    @Override
    public Page<BaseEntity> findPages(int page, int itemPerPage, String name) {
        return null;
    }

    public Optional<BaseEntity> findItemByProduct(ObservableList<BaseEntity> items, Product item) {
        return Optional.empty();
    }

    @Override
    public boolean deleteItem(BaseEntity item) {
        return false;
    }

    @Override
    public boolean isNew(BaseEntity item) {
        return false;
    }

    @Override
    public BaseEntity saveItem(BaseEntity item) {
        return null;
    }

    @Override
    public BaseEntity createDefault() {
        return null;
    }
}
