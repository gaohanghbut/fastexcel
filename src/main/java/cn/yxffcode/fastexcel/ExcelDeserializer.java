package cn.yxffcode.fastexcel;

/**
 * @author gaohang on 16/2/23.
 */
public interface ExcelDeserializer {
  Object fromString(String value, Class<?> type);
}
