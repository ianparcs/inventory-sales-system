package ph.parcs.rmhometiles.file.writer;

import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;

import java.util.List;

public class ProductExcelWriter extends ExcelWriter {

    public ProductExcelWriter(List<Product> products) {
        this.totalSize = products.size() + HEADER_SIZE;
        this.data = new Object[totalSize][];
        this.data[0] = new Object[]{"Code", "Name", "Description", "Category", "Stock", "Price"};
        this.headerColumnCount = data[0].length;
        for (int row = 1; row < totalSize; row++) {
            Product product = products.get(row - 1);
            data[row] = new Object[]{
                    product.getCode(), product.getName(), product.getDescription(),
                    createCategoryField(product.getCategory()), createStockField(product.getStock()),
                    product.getPrice().toString()};
        }

    }

    private Object createCategoryField(Category category) {
        if(category == null || category.getName().isEmpty()) return " ";
        return category.getName();
    }

    private Integer createStockField(Stock stock) {
        if(stock == null || stock.getStocks() <= 0) return 0;
        return stock.getStocks();
    }
}
