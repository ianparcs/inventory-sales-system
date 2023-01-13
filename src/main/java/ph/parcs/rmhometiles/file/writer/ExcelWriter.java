package ph.parcs.rmhometiles.file.writer;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;

public abstract class ExcelWriter {

    protected Object[][] data;
    protected Object[] header;
    protected Integer totalSize;
    protected Integer headerRow = 0;
    protected Integer HEADER_SIZE = 1;
    protected Integer headerColumnCount;

    protected XSSFSheet sheet;

    @SneakyThrows
    public void write() {
        Object[][] data = this.data;
        XSSFWorkbook workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Inventory");

        for (int rowNum = 0; rowNum < totalSize; rowNum++) {
            Row row = sheet.createRow(rowNum);
            for (int colNum = 0; colNum < data[rowNum].length; colNum++) {
                Cell cell = row.createCell(colNum);
                Object field = data[rowNum][colNum];
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Float) {
                    cell.setCellValue((Float) field);
                }
            }
        }

        for (int i = 0; i < headerColumnCount; i++) {
            sheet.setColumnWidth(i, 5000);
        }

        setHeaderStyle(workbook);
        setDataStyle(workbook, data.length);

        final String FILE_NAME = "RM-Inventory.xlsx";
        FileOutputStream out = new FileOutputStream(FILE_NAME);
        workbook.write(out);
        out.close();
    }

    private void setHeaderStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.WHITE.getIndex());

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.valueOf((short) 6));
        style.setBorderBottom(BorderStyle.valueOf((short) 1));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFont(font);

        Row row = sheet.getRow(0);
        for (int j = 0; j < headerColumnCount; j++) {
            row.getCell(j).setCellStyle(style);
        }
    }

    void setDataStyle(XSSFWorkbook workbook, Integer dataRowSize) {

        for (int i = 1; i < dataRowSize; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < headerColumnCount; j++) {
                Cell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                XSSFCellStyle style = workbook.createCellStyle();
                if (cellType == CellType.STRING) {
                    style.setAlignment(HorizontalAlignment.CENTER);
                } else {
                    style.setAlignment(HorizontalAlignment.RIGHT);
                }
                cell.setCellStyle(style);
            }
        }
    }

}
