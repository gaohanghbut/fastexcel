package cn.yxffcode.fastexcel;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要写入Excel的对象的属性
 *
 * @author gaohang on 16/1/25.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

  /**
   * 表头
   */
  String value() default StringUtils.EMPTY;

  /**
   * 序列化方式
   */
  Class<? extends ExcelSerializer> serializer() default ObjectSerializer.class;

  /**
   * 反序列化方式
   */
  Class<? extends ExcelDeserializer> deserializer() default DefaultExcelDeserializer.class;
}
