package ph.parcs.rmhometiles.entity.inventory.item;

public interface ItemActions<T> {

    T onItemDeleteAction(T item);

    T onItemEditAction(T item);

}
