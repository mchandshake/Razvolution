/* Decompiler 17ms, total 409ms, lines 140 */
package wtf.evolution.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import wtf.evolution.event.EventManager.1;
import wtf.evolution.event.events.Event;
import wtf.evolution.event.events.EventStoppable;
import wtf.evolution.event.types.Priority;

public class EventManager {
   private static final Map<Class<? extends Event>, List<wtf.evolution.event.EventManager.MethodData>> REGISTRY_MAP = new HashMap();

   public static void register(Object object) {
      Method[] var1 = object.getClass().getDeclaredMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method method = var1[var3];
         if (!isMethodBad(method)) {
            register(method, object);
         }
      }

   }

   public static void unregister(Object object) {
      Iterator var1 = REGISTRY_MAP.values().iterator();

      while(var1.hasNext()) {
         List<wtf.evolution.event.EventManager.MethodData> dataList = (List)var1.next();
         dataList.removeIf((data) -> {
            return data.getSource().equals(object);
         });
      }

      cleanMap(true);
   }

   private static void register(Method method, Object object) {
      Class<? extends Event> indexClass = method.getParameterTypes()[0];
      wtf.evolution.event.EventManager.MethodData data = new wtf.evolution.event.EventManager.MethodData(object, method, ((EventTarget)method.getAnnotation(EventTarget.class)).value());
      if (!data.getTarget().isAccessible()) {
         data.getTarget().setAccessible(true);
      }

      if (REGISTRY_MAP.containsKey(indexClass)) {
         if (!((List)REGISTRY_MAP.get(indexClass)).contains(data)) {
            ((List)REGISTRY_MAP.get(indexClass)).add(data);
            sortListValue(indexClass);
         }
      } else {
         REGISTRY_MAP.put(indexClass, new 1(data));
      }

   }

   public static void cleanMap(boolean onlyEmptyEntries) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if (!mapIterator.hasNext()) {
               return;
            }
         } while(onlyEmptyEntries && !((List)((Entry)mapIterator.next()).getValue()).isEmpty());

         mapIterator.remove();
      }
   }

   private static void sortListValue(Class<? extends Event> indexClass) {
      List<wtf.evolution.event.EventManager.MethodData> sortedList = new CopyOnWriteArrayList();
      byte[] var2 = Priority.VALUE_ARRAY;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte priority = var2[var4];
         Iterator var6 = ((List)REGISTRY_MAP.get(indexClass)).iterator();

         while(var6.hasNext()) {
            wtf.evolution.event.EventManager.MethodData data = (wtf.evolution.event.EventManager.MethodData)var6.next();
            if (data.getPriority() == priority) {
               sortedList.add(data);
            }
         }
      }

      REGISTRY_MAP.put(indexClass, sortedList);
   }

   private static boolean isMethodBad(Method method) {
      return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
      return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static Event call(Event event) {
      List<wtf.evolution.event.EventManager.MethodData> dataList = (List)REGISTRY_MAP.get(event.getClass());
      if (dataList != null) {
         if (event instanceof EventStoppable) {
            EventStoppable stoppable = (EventStoppable)event;
            Iterator var3 = dataList.iterator();

            while(var3.hasNext()) {
               wtf.evolution.event.EventManager.MethodData data = (wtf.evolution.event.EventManager.MethodData)var3.next();
               invoke(data, event);
               if (stoppable.isStopped()) {
                  break;
               }
            }
         } else {
            Iterator var5 = dataList.iterator();

            while(var5.hasNext()) {
               wtf.evolution.event.EventManager.MethodData data = (wtf.evolution.event.EventManager.MethodData)var5.next();
               invoke(data, event);
            }
         }
      }

      return event;
   }

   private static void invoke(wtf.evolution.event.EventManager.MethodData data, Event argument) {
      try {
         data.getTarget().invoke(data.getSource(), argument);
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
      }

   }
}
