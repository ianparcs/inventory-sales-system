package ph.parcs.rmhometiles;

public interface ItemListener<T> {

    void onSavedSuccess(T entity);

    void onSaveFailed(T savedItem);
}
