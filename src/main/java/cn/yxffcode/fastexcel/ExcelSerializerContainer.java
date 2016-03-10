package cn.yxffcode.fastexcel;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

import static cn.yxffcode.fastexcel.Reflections.instantiate;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author gaohang on 16/1/25.
 */
public final class ExcelSerializerContainer {

  private static final Object DESERIALIZER_LOCK = new Object();
  private static final Object SERIALIZER_LOCK = new Object();

  private static ConcurrentMap<Class<?>, ExcelSerializer> serializers = Maps.newConcurrentMap();
  private static ConcurrentMap<Class<?>, ExcelDeserializer> deserializers = Maps.newConcurrentMap();

  private ExcelSerializerContainer() {
  }

  public static ExcelSerializer getSerializer(Class<?> c) {
    checkNotNull(c);
    ExcelSerializer excelSerializer = serializers.get(c);
    if (excelSerializer == null) {
      synchronized (SERIALIZER_LOCK) {
        excelSerializer = serializers.get(c);
        if (excelSerializer == null) {
          excelSerializer = (ExcelSerializer) instantiate(c);
          serializers.putIfAbsent(c, excelSerializer);
        }
      }
    }
    return excelSerializer;
  }

  public static ExcelDeserializer getDeserializer(Class<?> c) {
    checkNotNull(c);
    ExcelDeserializer excelDeserializer = deserializers.get(c);
    if (excelDeserializer == null) {
      synchronized (DESERIALIZER_LOCK) {
        excelDeserializer = deserializers.get(c);
        if (excelDeserializer == null) {
          excelDeserializer = (ExcelDeserializer) instantiate(c);
          deserializers.putIfAbsent(c, excelDeserializer);
        }
      }
    }
    return excelDeserializer;
  }

}
