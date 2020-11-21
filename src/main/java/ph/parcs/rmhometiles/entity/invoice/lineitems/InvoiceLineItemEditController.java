package ph.parcs.rmhometiles.entity.invoice.lineitems;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

@Controller
public class InvoiceLineItemEditController extends EditItemController<InvoiceLineItem> {

    @FXML
    private Label lblDescription;
    @FXML
    private Label lblCodeName;
    @FXML
    private Label lblQuantity;
    @FXML
    private Label lblStock;
    @FXML
    private Label lblAmount;
    @FXML
    private Label lblPrice;

    @Override
    protected InvoiceLineItem unbindFields(Integer id) {
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setId(id);
        return invoiceLineItem;
    }

    @Override
    protected void bindFields(InvoiceLineItem lineItem) {
        if (lineItem.getProduct() == null) return;
        Product product = lineItem.getProduct();

        if (!StringUtils.isEmpty(lineItem.getCode())) lblCodeName.setText(lineItem.getName());
        if (!StringUtils.isEmpty(product.getDescriptionProperty()))
            lblDescription.setText(product.getDescriptionProperty());
        if (!StringUtils.isEmpty(lineItem.getQuantity())) lblQuantity.setText(lineItem.getQuantity() + "");
        ///      if (!StringUtils.isEmpty(product.getPrice())) lblPrice.setText(product.getPrice().toString());


        //   NumberBinding amountBinding = product.getPrice().multiply(Integer.parseInt(lblQuantity.getText()));
        // float amount = amountBinding.getValue().floatValue();
        //lblAmount.setText(Constant.PESO_SIGN + amount);

        // create a monetary value
        Money money = Money.parse("23.87");
        CurrencyUnit usd = CurrencyUnit.of("PHP");

    }

    @Override
    protected void clearFields() {

    }

    @Override
    protected void setDialogTitle(InvoiceLineItem item) {
        lblTitle.setText("Invoice");
    }

    @Autowired
    public void setLineItemService(InvoiceLineItemService invoiceLineItemService) {
        this.baseService = invoiceLineItemService;
    }
}
