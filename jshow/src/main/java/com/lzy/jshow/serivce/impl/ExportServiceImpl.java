package com.lzy.jshow.serivce.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.lzy.jshow.dto.FillDataDTO;
import com.lzy.jshow.serivce.ExportService;
import com.lzy.jshow.utils.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
    @Override
    public ByteArrayOutputStream export1() {
        InputStream templateStream = getClass().getResourceAsStream("/templates/fill_excel.xlsx");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<FillDataDTO> data = getData();

        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(templateStream).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig horConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            excelWriter.fill(new FillWrapper("data1",data),writeSheet);
            excelWriter.fill(new FillWrapper("data2",data),horConfig,writeSheet);
            excelWriter.fill(new FillWrapper("data3",data),horConfig,writeSheet);

            Map<String, Object> map = new HashMap<>();
            map.put("nowDate", DateUtils.getTimeString());
            map.put("author", "jerry");
            map.put("allTotal",50);
            excelWriter.fill(map, writeSheet);

            WriteWorkbookHolder writeWorkbookHolder = excelWriter.writeContext().writeWorkbookHolder();
            WriteSheet writeSheet1 = EasyExcel.writerSheet(1).build();
            excelWriter.fill(map,writeSheet1);

            Sheet firstSheet = writeWorkbookHolder.getWorkbook().getSheetAt(1);
            withPoiCreate(firstSheet,3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    private void withPoiCreate(Sheet sheet, int startIndex) {
        CellStyle cellTitleStyle = createCellTitleStyle(sheet);
        CellStyle cellDataStyle = createCellDataStyle(sheet);
        List<Map<String, Object>> dynamicData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("cost","cost_centre" + i);
            map.put("itemCode","item_code" + i);
            map.put("item","item" + i);
            map.put("allocation","20000");
            map.put("forecast","25000");
            map.put("requirement","22000");
            map.put("actual","10000");
            map.put("spent","30000");
            map.put("spentAllocation","40000");
            dynamicData.add(map);
        }
        writeModule(sheet, dynamicData, startIndex, 0, "title1", cellTitleStyle, cellDataStyle);
    }

    private void writeModule(Sheet sheet, List<Map<String, Object>> data,
                             int startRowIndex, int startColIndex, String title,
                             CellStyle titleStyle, CellStyle dataStyle) {
        int numRowsToInsert = data.size();
        int lastRow = sheet.getLastRowNum();

        if (lastRow >= startRowIndex) {
            sheet.shiftRows(startRowIndex, lastRow, numRowsToInsert, true, false);
        }

        int titleRowIndex = startRowIndex - 1;
        Row titleRow = sheet.createRow(titleRowIndex);
        CellRangeAddress titleRange = new CellRangeAddress(
                titleRowIndex, titleRowIndex, startColIndex, startColIndex + 8
        );
        sheet.addMergedRegion(titleRange);

        Cell titleCell = titleRow.createCell(startColIndex);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> rowData = data.get(i);
            Row row = sheet.getRow(startRowIndex + i);
            if (row == null) row = sheet.createRow(startRowIndex + i);

            writeCell(row, startColIndex, rowData, "0", dataStyle);
            writeCell(row, startColIndex + 1, rowData, "1", dataStyle);
            writeCell(row, startColIndex + 2, rowData, "2", dataStyle);
            writeCell(row, startColIndex + 3, rowData, "3", dataStyle);
            writeCell(row, startColIndex + 4, rowData, "4", dataStyle);
            writeCell(row, startColIndex + 5, rowData, "5", dataStyle);
            writeCell(row, startColIndex + 6, rowData, "6", dataStyle);
            writeCell(row, startColIndex + 7, rowData, "7", dataStyle);
            writeCell(row, startColIndex + 8, rowData, "8", dataStyle);
        }
    }

    private void writeCell(Row row, int colIndex, Map<String, Object> data, String fieldSuffix, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        String fieldName = data.keySet().toArray()[Integer.parseInt(fieldSuffix)].toString();
        cell.setCellValue(String.valueOf(data.get(fieldName)));
        cell.setCellStyle(style);
    }



    private CellStyle createCellTitleStyle(Sheet sheet){
        Workbook workbook = sheet.getWorkbook();
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Times New Roman");
        titleFont.setFontHeightInPoints((short) 24);
        titleFont.setColor(IndexedColors.RED.getIndex());
        titleFont.setBold(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return titleStyle;
    }

    private CellStyle createCellDataStyle(Sheet sheet){
        Workbook workbook = sheet.getWorkbook();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    @Override
    public ByteArrayOutputStream export2() {
        return null;
    }

    private List<FillDataDTO> getData(){
        List<FillDataDTO> sData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FillDataDTO dto = new FillDataDTO();
            dto.setName("name:" + i);
            dto.setVal("val:" + i);
            dto.setTotal(Double.valueOf(i));
            dto.setDescription("description:" + i);
            dto.setTotal(27d);
            sData.add(dto);
        }
        return sData;
    }
}
