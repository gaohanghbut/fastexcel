package cn.yxffcode.fastexcel;

/**
 * @author gaohang on 16/2/23.
 */
public class DeserializerException extends RuntimeException {
  public DeserializerException() {
  }

  public DeserializerException(String message) {
    super(message);
  }

  public DeserializerException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeserializerException(Throwable cause) {
    super(cause);
  }

  public DeserializerException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
