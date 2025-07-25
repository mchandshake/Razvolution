/* Decompiler 1ms, total 278ms, lines 14 */
package wtf.evolution.helpers;

import java.lang.reflect.Field;

public class ReflectionUtil {
   public static Field getField(Class<?> clazz, String fieldName) {
      try {
         return clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException var3) {
         return null;
      }
   }
}
