package ph.parcs.rmhometiles.util.converter;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.util.Global;

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
