/* Decompiler 5ms, total 304ms, lines 68 */
package wtf.evolution.editor;

import wtf.evolution.module.Module;

public class Drag {
   public final Module module;
   public final String name;
   private float width;
   private float height;
   public boolean dragging;
   public float startX;
   public float startY;
   public float xPos;
   public float yPos;

   public Drag(Module module, String name, float initialXVal, float initialYVal) {
      this.module = module;
      this.name = name;
      this.xPos = initialXVal;
      this.yPos = initialYVal;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public float getX() {
      return this.xPos;
   }

   public void setX(float x) {
      this.xPos = x;
   }

   public float getY() {
      return this.yPos;
   }

   public void setY(float y) {
      this.yPos = y;
   }

   public void onClick(int mouseX, int mouseY) {
      if ((float)mouseX >= this.xPos && (float)mouseX <= this.xPos + this.width && (float)mouseY >= this.yPos && (float)mouseY <= this.yPos + this.height) {
         this.dragging = true;
         this.startX = (float)mouseX - this.xPos;
         this.startY = (float)mouseY - this.yPos;
      }

   }

   public void onRelease(int mouseX, int mouseY) {
      this.dragging = false;
   }
}
