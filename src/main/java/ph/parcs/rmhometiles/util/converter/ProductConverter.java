package ph.parcs.rmhometiles.util.converter;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.util.Global;

public class ProductConverter extends StringConverter<BaseEntity> {

    private Product product;

    public ProductConverter(BaseEntity baseEntity) {
        this.product = (Product) baseEntity;
    }

    @Override
    public String toString(BaseEntity baseEntity) {
        Product product = (Product) baseEntity;
        if (baseEntity == null) return Global.STRING_EMPTY;
        return product.getCode();
    }

    @Override
    public Product fromString(String s) {
        return product;
    }
}
