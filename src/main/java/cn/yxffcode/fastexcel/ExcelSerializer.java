package cn.yxffcode.fastexcel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 用于定义写入Excel单元格的方式
 *
 * @author gaohang on 16/1/25.
 */
public interface ExcelSerializer {
  Object serialize(Cell cell, Object value);
}
