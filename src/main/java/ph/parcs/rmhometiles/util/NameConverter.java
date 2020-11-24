package ph.parcs.rmhometiles.util;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

public class NameConverter extends StringConverter<BaseEntity> {

    private BaseEntity baseEntity;

    public NameConverter(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    @Override
    public String toString(BaseEntity baseEntity) {
        if (baseEntity == null) return Global.STRING_EMPTY;
        return baseEntity.getName();
    }

    @Override
    public BaseEntity fromString(String s) {
        return baseEntity;
    }
}
