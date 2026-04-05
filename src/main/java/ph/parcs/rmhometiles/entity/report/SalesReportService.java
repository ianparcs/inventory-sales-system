package ph.parcs.rmhometiles.entity.report;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;
import ph.parcs.rmhometiles.entity.money.MoneyService;
import ph.parcs.rmhometiles.util.date.DateRangeType;
import ph.parcs.rmhometiles.util.date.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SalesReportService {

    private InvoiceService invoiceService;
    private MoneyService moneyService;

    public List<SalesReport> createSalesReports(DateRangeType dateRangeType) {
        if (dateRangeType.equals(DateRangeType.ALL_TIME)) {
            return extractInvoice(invoiceService.findAllInvoice());
        }

        LocalDateTime[] dateTimeRange = DateUtil.find(dateRangeType);
        List<Invoice> invoices = invoiceService.findAllInvoiceByDate(dateTimeRange);

        return extractInvoice(invoices);
    }

    private List<SalesReport> extractInvoice(List<Invoice> invoices) {
        List<SalesReport> salesReports = new ArrayList<>();

        if (invoices == null) return new ArrayList<>();

        Map<LocalDateTime, List<Invoice>> groupedByDate = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getCreatedAt));


        for (var entry : groupedByDate.entrySet()) {
            List<Invoice> groupInvoice = entry.getValue();

            SalesReport salesReport = new SalesReport();
            Money tax = moneyService.parseMoney("0.00");
            Money cost = moneyService.parseMoney("0.00");
            Money total = moneyService.parseMoney("0.00");
            Money subtotal = moneyService.parseMoney("0.00");

            for (Invoice invoice : groupInvoice) {
                tax = tax.plus(invoice.getTaxAmount());
                cost = cost.plus(invoice.getItemCosts());
                total = total.plus(invoice.getTotalAmount());
                subtotal = subtotal.plus(invoice.getTotalAmount());
                salesReport.setCreatedAt(invoice.getCreatedAt());
            }
            salesReport.setTax(tax);
            salesReport.setCost(cost);
            salesReport.setTotal(total);
            salesReports.add(salesReport);
            salesReport.setSubtotal(subtotal);
        }

        return salesReports;
    }

    @Autowired
    public void setMoneyService(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
}
