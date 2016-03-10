package cn.yxffcode.fastexcel;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Date;

/**
 * @author gaohang on 16/1/25.
 */
public class ObjectSerializer implements ExcelSerializer {
  @Override public Object serialize(Cell cell, Object value) {
    if (value instanceof Number || value instanceof Date) {
      return value;
    }
    return value.toString();
  }
}
