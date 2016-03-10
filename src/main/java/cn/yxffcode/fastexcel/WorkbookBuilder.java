package cn.yxffcode.fastexcel;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 用于构建表单
 *
 * @author gaohang on 16/3/10.
 */
public final class WorkbookBuilder {
  private static final int DEFAULT_COLUMN_WIDTH = 12;

  public static WorkbookBuilder withWorkbook(Workbook workbook) {
    checkNotNull(workbook);
    return new WorkbookBuilder(workbook);
  }

  private final Workbook workbook;

  private Iterable<?> data;

  private String sheetName = StringUtils.EMPTY;

  private int columnWidth = DEFAULT_COLUMN_WIDTH;

  private WorkbookBuilder(Workbook workbook) {
    this.workbook = workbook;
  }

  public WorkbookBuilder data(Iterable<?> data) {
    this.data = data;
    return this;
  }

  public WorkbookBuilder sheetName(String sheetName) {
    if (sheetName != null) {
      this.sheetName = sheetName;
    } else {
      this.sheetName = StringUtils.EMPTY;
    }
    return this;
  }

  public WorkbookBuilder columnWidth(int columnWidth) {
    checkArgument(columnWidth > 0);
    this.columnWidth = columnWidth;
    return this;
  }

  public Workbook build() {
    Sheet sheet = workbook.createSheet(sheetName);
    sheet.setDefaultColumnWidth(columnWidth);

    Field[] declaredFields = null;
    int row = -1;
    for (Object obj : data) {
      if (declaredFields == null) {
        declaredFields = obj.getClass().getDeclaredFields();
        //写表头
        for (int i = 0; i < declaredFields.length; i++) {
          Field declaredField = declaredFields[i];
          Column column = declaredField.getAnnotation(Column.class);
          if (column != null && StringUtils.isNotBlank(column.value())) {
            if (row < 0) {
              row = 0;
            }
            //表头
            Cell cell = getCell(sheet, row, i);
            cell.setCellValue(column.value());
          }
        }
      }
      //写值
      ++row;
      for (int i = 0; i < declaredFields.length; i++) {
        Field declaredField = declaredFields[i];
        if (!declaredField.isAccessible()) {
          declaredField.setAccessible(true);
        }
        Object fieldValue = getFieldValue(obj, declaredField);
        if (fieldValue == null) {
          continue;
        }
        Column column = declaredField.getAnnotation(Column.class);
        Class<? extends ExcelSerializer> serializerClass;
        if (column == null) {
          serializerClass = ObjectSerializer.class;
        } else {
          serializerClass = column.serializer();
        }
        ExcelSerializer excelSerializer = ExcelSerializerContainer.getSerializer(serializerClass);

        Cell cell = getCell(sheet, row, i);
        Object serializedValue = excelSerializer.serialize(cell, fieldValue);
        if (serializedValue == null) {
          continue;
        }
        if (serializedValue instanceof Number) {
          cell.setCellValue(((Number) serializedValue).doubleValue());
        } else if (serializedValue instanceof Date) {
          cell.setCellValue((Date) serializedValue);
        } else if (serializedValue instanceof Boolean) {
          cell.setCellValue((Boolean) serializedValue);
        } else if (serializedValue instanceof RichTextString) {
          cell.setCellValue((RichTextString) serializedValue);
        } else {
          cell.setCellValue(serializedValue.toString());
        }
      }
    }
    return this.workbook;
  }

  private Cell getCell(Sheet sheet, int row, int col) {
    Row sheetRow = sheet.getRow(row);
    if (sheetRow == null) {
      sheetRow = sheet.createRow(row);
    }
    Cell cell = sheetRow.getCell(col);
    if (cell == null) {
      cell = sheetRow.createCell(col);
    }
    return cell;
  }

  private Object getFieldValue(Object obj, Field declaredField) {
    try {
      return declaredField.get(obj);
    } catch (IllegalAccessException e) {
      Throwables.propagate(e);
      return null;
    }
  }

}
