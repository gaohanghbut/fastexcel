package cn.yxffcode.fastexcel;

import com.google.common.base.Throwables;

import java.lang.reflect.Field;

/**
 * @author gaohang on 16/3/10.
 */
public final class Reflections {
  private Reflections() {
  }

  public static <T> void setField(Field field, T element, Object cellValue) {
    try {
      field.set(cellValue, element);
    } catch (IllegalAccessException ex) {
      Throwables.propagate(ex);
    }
  }

  public static <T> T instantiate(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception ex) {
      Throwables.propagate(ex);
      return null;
    }
  }
}
