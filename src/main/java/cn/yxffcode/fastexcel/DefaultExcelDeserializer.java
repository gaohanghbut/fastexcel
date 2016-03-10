package cn.yxffcode.fastexcel;

import com.google.common.primitives.Primitives;

import java.util.Date;

/**
 * @author gaohang on 16/2/23.
 */
public class DefaultExcelDeserializer implements ExcelDeserializer {
  @Override public Object fromString(String value, Class<?> type) {
    if (type == String.class) {
      return value;
    }
    if (type.isAssignableFrom(Date.class)) {
      ExcelDeserializer deserializer =
          ExcelSerializerContainer.getDeserializer(DateDeserializer.class);
      Date date = (Date) deserializer.fromString(value, Date.class);

      if (type == Date.class) {
        return date;
      }
      if (type == java.sql.Date.class) {
        return new java.sql.Date(date.getTime());
      }
      if (type == java.sql.Time.class) {
        return new java.sql.Time(date.getTime());
      }
      if (type == java.sql.Timestamp.class) {
        return new java.sql.Timestamp(date.getTime());
      }
    }
    if (type.isAssignableFrom(Number.class) || Primitives.allPrimitiveTypes().contains(type)) {
      ExcelDeserializer deserializer =
          ExcelSerializerContainer.getDeserializer(PrimitiveDeserializer.class);
      return deserializer.fromString(value, type);
    }
    throw new DeserializerException("不支持的序列化类型:" + type.getName());
  }
}
