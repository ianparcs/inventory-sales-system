package ph.parcs.rmhometiles.entity.inventory.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.exception.ItemLockedException;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.List;

@Service
public abstract class BaseService<T extends BaseEntity> implements BaseServiceInterface<T, Integer> {

    protected EntityRepository<T, Integer> entityRepository;

    @Override
    public Page<T> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return entityRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public T findEntityById(Integer id) {
        return entityRepository.findById(id).orElse(null);
    }

    @Override
    public List<T> findEntities(String query) {
        return entityRepository.findAllByNameContains(query);
    }

    public boolean isItemsExists(List<T> items) {
        for (T item : items) {
            boolean isExist = entityRepository.existsById(item.getId());
            if (isExist) return true;
        }
        return false;
    }

    @Override
    public T saveEntity(T entity) {
        return entityRepository.save(entity);
    }

    @Override
    public boolean deleteEntity(T entity) throws ItemLockedException {
        entityRepository.delete(entity);
        return !isExist(entity.getId());
    }

    @Override
    public boolean isExist(Integer id) {
        return entityRepository.existsById(id);
    }

}