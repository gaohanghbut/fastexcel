package cn.yxffcode.fastexcel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.yxffcode.fastexcel.Reflections.instantiate;
import static cn.yxffcode.fastexcel.Reflections.setField;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author gaohang on 16/2/23.
 */
public class SimpleExcelTable {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExcelTable.class);

  public static SimpleExcelTable createXlsx(InputStream in) throws IOException {
    checkNotNull(in);
    XSSFWorkbook sheets = new XSSFWorkbook(in);
    return new SimpleExcelTable(sheets);
  }

  private final Workbook workbook;

  private SimpleExcelTable(Workbook workbook) {
    this.workbook = workbook;
  }

  public <T> List<T> map(Class<? extends T> valueType) {
    Sheet sheet = workbook.getSheetAt(0);
    if (sheet == null) {
      return Collections.emptyList();
    }
    //第一行为表头
    Row columnNames = sheet.getRow(0);
    if (columnNames == null) {
      return Collections.emptyList();
    }
    //resolve column-property mapping
    Field[] fields = valueType.getDeclaredFields();
    Map<String, Field> columnMapping = Maps.newHashMap();
    for (Field field : fields) {
      Column annotation = field.getAnnotation(Column.class);
      if (annotation != null) {
        columnMapping.put(annotation.value(), field);
      }
    }

    List<T> elems = Lists.newArrayList();
    for (int i = 1; ; i++) {
      Row row = sheet.getRow(i);
      if (row == null) {
        break;
      }
      T element = instantiate(valueType);
      elems.add(element);
      for (int j = 0; ; j++) {
        Cell cell = row.getCell(j);
        if (cell == null) {
          break;
        }
        Cell columnCell = columnNames.getCell(j);
        if (columnCell == null) {
          break;
        }
        String columnName = columnCell.getStringCellValue();
        if (isBlank(columnName)) {
          LOGGER.warn("skipped blank column name, column index={}, value={}",
              j,
              columnCell.getStringCellValue());
          continue;
        }
        Field field = columnMapping.get(columnName.trim());
        if (field == null) {
          LOGGER.warn("skipped not mapped column, column index={}, value={}",
              j,
              columnCell.getStringCellValue());
          continue;
        }
        Column annotation = field.getAnnotation(Column.class);
        Class<? extends ExcelDeserializer> deserializer = annotation.deserializer();
        ExcelDeserializer deserializerInstance =
            ExcelSerializerContainer.getDeserializer(deserializer);
        Object cellValue;
        int cellType = cell.getCellType();
        switch (cellType) {
          case Cell.CELL_TYPE_NUMERIC:
            cellValue =
                deserializerInstance
                    .fromString(Double.toString(cell.getNumericCellValue()), field.getType());
            break;
          case Cell.CELL_TYPE_STRING:
            cellValue =
                deserializerInstance.fromString(cell.getStringCellValue().trim(), field.getType());
            break;
          case Cell.CELL_TYPE_BLANK:
            cellValue = 0;
            break;
          default:
            throw new IllegalStateException("不支持的样式");
        }

        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        setField(field, element, cellValue);
      }
    }
    return elems;
  }

}
