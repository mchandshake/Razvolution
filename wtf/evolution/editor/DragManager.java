/* Decompiler 5ms, total 317ms, lines 62 */
package wtf.evolution.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Iterator;

public class DragManager {
   public static HashMap<String, Drag> draggables = new HashMap();
   private static final File DRAG_DATA = new File("evolution", "drag.cfg");
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setLenient().create();

   public static void saveDragData() {
      if (!DRAG_DATA.exists()) {
         DRAG_DATA.getParentFile().mkdirs();
      }

      try {
         String ss = "";

         Drag drag;
         for(Iterator var1 = draggables.values().iterator(); var1.hasNext(); ss = ss + drag.name + ":" + drag.getX() + ":" + drag.getY() + "\n") {
            drag = (Drag)var1.next();
         }

         Files.write(DRAG_DATA.toPath(), ss.getBytes(), new OpenOption[0]);
      } catch (IOException var3) {
         var3.printStackTrace();
         System.out.println("Failed to save draggables");
      }

   }

   public static void loadDragData() {
      try {
         if (!DRAG_DATA.exists()) {
            System.out.println("No dragg data found");
            return;
         }

         Iterator var0 = Files.readAllLines(DRAG_DATA.toPath()).iterator();

         while(var0.hasNext()) {
            String ss = (String)var0.next();
            String[] split = ss.split(":");
            Drag currentDrag = (Drag)draggables.get(split[0]);
            currentDrag.setX(Float.parseFloat(split[1]));
            currentDrag.setY(Float.parseFloat(split[2]));
            draggables.put(split[0], currentDrag);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
         System.out.println("Failed to load draggables");
      }

   }
}
