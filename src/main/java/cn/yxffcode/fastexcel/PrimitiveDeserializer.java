package cn.yxffcode.fastexcel;

import com.google.common.primitives.Primitives;

/**
 * @author gaohang on 16/2/23.
 */
public class PrimitiveDeserializer implements ExcelDeserializer {
  @Override public Object fromString(String value, Class<?> type) {
    if (Primitives.allPrimitiveTypes().contains(type)) {
      return fromString(value, Primitives.wrap(type));
    }
    if (type == Boolean.class) {
      return Boolean.valueOf(value);
    }
    double v = Double.parseDouble(value);
    if (type == Double.class) {
      return v;
    }
    if (type == Float.class) {
      return (float) v;
    }
    //// FIXME: 16/2/25 可能不是int
    int iv = (int) v;
    return iv;
  }
}
