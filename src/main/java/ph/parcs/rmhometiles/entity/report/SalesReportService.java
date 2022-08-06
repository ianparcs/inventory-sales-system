package ph.parcs.rmhometiles.entity.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceRepository;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.util.DateUtility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SalesReportService {

    private InvoiceRepository invoiceRepository;

    public List<SalesReport> findReports(String dateRangeText) {
        LocalDateTime[] dateTimeRange = DateUtility.findDate(dateRangeText);

        LocalDateTime startTime = dateTimeRange[0];
        LocalDateTime endTime = dateTimeRange[1];

        List<Invoice> invoices = invoiceRepository.findAllByCreatedAtBetween(startTime, endTime);

        return createSalesReport(invoices);
    }

    private List<SalesReport> createSalesReport(List<Invoice> invoices) {
        List<SalesReport> salesReports = new ArrayList<>();

        if (invoices != null) {
            for (Invoice invoice : invoices) {
                SalesReport salesReport = new SalesReport();
                salesReports.add(salesReport);
                salesReport.setCreatedAt(invoice.getCreatedAt());
            }
        }
        return salesReports;
    }

    @Autowired
    public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
}
