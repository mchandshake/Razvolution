/* Decompiler 0ms, total 254ms, lines 10 */
package wtf.evolution.settings.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
   JsonObject save();

   void load(JsonObject var1);
}
