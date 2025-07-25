/* Decompiler 0ms, total 336ms, lines 12 */
package wtf.evolution.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
   String name();

   Category type();
}
