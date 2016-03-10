package cn.yxffcode.fastexcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author gaohang on 16/3/10.
 */
public class DateDeserializer implements ExcelDeserializer {

  private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd:hh:mm:ss";

  @Override public Object fromString(String value, Class<?> type) {
    checkArgument(type == Date.class);
    SimpleDateFormat fmt = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    try {
      return fmt.parse(value);
    } catch (ParseException e) {
      throw new DeserializerException(
          "deserialize failed:value=" + value + " type=" + type.getName(), e);
    }
  }
}
