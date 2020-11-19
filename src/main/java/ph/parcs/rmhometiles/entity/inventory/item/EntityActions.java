package ph.parcs.rmhometiles.entity.inventory.item;

public interface EntityActions<T> {

    T onDeleteActionClick(T item);

    T onEditActionClick(T item);

}
