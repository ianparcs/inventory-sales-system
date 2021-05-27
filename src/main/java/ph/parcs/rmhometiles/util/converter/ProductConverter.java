package ph.parcs.rmhometiles.util.converter;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.util.Global;

public class ProductConverter extends StringConverter<Product> {

    private Product product;

    public ProductConverter(Product product) {
        this.product = product;
    }

    @Override
    public String toString(Product product) {
        if (product == null) return Global.STRING_EMPTY;
        return product.getCode();
    }

    @Override
    public Product fromString(String s) {
        return product;
    }
}
