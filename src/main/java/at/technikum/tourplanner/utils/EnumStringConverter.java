package at.technikum.tourplanner.utils;

import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class EnumStringConverter<T extends Enum<T>> extends StringConverter<T> {
  private final Class<T> enumType;
  private final Method getMethod;

  public EnumStringConverter(Class<T> enumType) {
    this.enumType = enumType;
    this.getMethod = findGetMethod(enumType);
  }

  private Method findGetMethod(Class<T> enumType) {
    try {
      return enumType.getMethod("get"); // Look for "get()" method
    } catch (NoSuchMethodException e) {
      return null; // If no "get()" method, return null
    }
  }

  @Override
  public String toString(T enumValue) {
    if (enumValue == null) return "";

    try {
      // If the enum has a "get()" method, call it
      if (getMethod != null) {
        return (String) getMethod.invoke(enumValue);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    // Fallback to default enum name
    return enumValue.name();
  }

  @Override
  public T fromString(String string) {
    return Arrays.stream(enumType.getEnumConstants())
            .filter(e -> {
              try {
                return getMethod != null && getMethod.invoke(e).equals(string);
              } catch (Exception ex) {
                return e.name().equals(string); // Fallback
              }
            })
            .findFirst()
            .orElse(null);
  }
}
