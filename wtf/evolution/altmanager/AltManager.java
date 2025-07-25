/* Decompiler 45ms, total 712ms, lines 172 */
package wtf.evolution.altmanager;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;

public class AltManager extends GuiScreen {
   public boolean login;
   public String loginString = "";
   public Session ss = new Session(RandomStringUtils.randomAlphabetic(5));
   public double mouseX1;
   public double yAnimated;
   public static ArrayList<Session> sessions = new ArrayList();
   public static final File file;

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.drawDefaultBackground();
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      RenderUtil.drawRect(0.0F, 0.0F, (float)sr.getScaledWidth(), (float)sr.getScaledHeight(), (new Color(24, 24, 25, 255)).getRGB());
      RenderUtil.drawBlurredShadow(45.0F, 85.0F, 120.0F, 30.0F, 15, Color.BLACK);
      RoundedUtil.drawRound(45.0F, 85.0F, 120.0F, 30.0F, 3.0F, new Color(19, 19, 19));
      Fonts.RUB16.drawString("Login", 55.0F, 90.0F, (new Color(103, 103, 103, 255)).getRGB());
      RoundedUtil.drawRound(50.0F, 100.0F, 110.0F, 10.0F, 2.0F, new Color(34, 34, 35, 255));
      Fonts.RUB16.drawString(this.loginString + (this.login ? (System.currentTimeMillis() % 1000L >= 500L ? "" : "_") : ""), 55.0F, 103.0F, (new Color(103, 103, 103, 255)).getRGB());
      RenderUtil.drawBlurredShadow(500.0F, 50.0F, 400.0F, 400.0F, 15, Color.BLACK);
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.prepareScissorBox(500.0F, 50.0F, 900.0F, 450.0F);
      RoundedUtil.drawRound(500.0F, 50.0F, 400.0F, 400.0F, 10.0F, new Color(34, 34, 35, 255));
      this.mouseX1 += (double)(Integer.compare(Mouse.getDWheel(), 0) * 15);
      double x = 505.0D;
      double y = 60.0D + this.mouseX1;
      int count = 0;
      Iterator var10 = sessions.iterator();

      while(var10.hasNext()) {
         Session s = (Session)var10.next();
         if (s == this.ss) {
            RenderUtil.drawBlurredShadow((float)x, (float)y, 90.0F, 30.0F, 15, Color.BLACK);
         }

         RoundedUtil.drawRound((float)x, (float)y, 90.0F, 30.0F, 5.0F, new Color(50, 50, 50, 255));
         Fonts.RUB16.drawString(s.nick, (float)x + 85.0F - (float)Fonts.RUB16.getStringWidth(s.nick), (float)y + 5.0F, (new Color(103, 103, 103, 255)).getRGB());
         Fonts.RUB16.drawString(s == this.ss ? "Joined." : "", (float)x + 85.0F - (float)Fonts.RUB16.getStringWidth("Joined."), (float)y + 15.0F, (new Color(255, 255, 255, 200)).getRGB());
         RenderUtil.downloadImage("https://minotar.net/avatar/" + s.nick + "/100.png", (float)x + 3.0F, (float)y + 3.0F, 23.0F, 23.0F);
         if (x >= (double)(sr.getScaledWidth() - 200)) {
            x = 505.0D;
            y += 40.0D;
            ++count;
         } else {
            x += 100.0D;
         }
      }

      if (this.mouseX1 > (double)count) {
         this.mouseX1 = (double)count;
      }

      if (this.mouseX1 < -y * (double)count / 10.0D) {
         this.mouseX1 = -y * (double)count / 10.0D;
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      Fonts.RUB16.drawCenteredString(sessions.size() + " - alts.", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() - 30), (new Color(103, 103, 103, 255)).getRGB());
      Fonts.RUB12.drawCenteredString("press enter to generate alt.", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() - 20), (new Color(103, 103, 103, 255)).getRGB());
      Fonts.RUB12.drawCenteredString("logined at " + this.ss.nick + ".", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() - 10), (new Color(103, 103, 103, 255)).getRGB());
   }

   public AltManager() {
      try {
         if (!file.exists()) {
            return;
         }

         FileReader fr = new FileReader(file);
         Scanner scan = new Scanner(fr);

         while(scan.hasNextLine()) {
            String[] line = scan.nextLine().split(":");
            if (!sessions.contains(new Session(line[0]))) {
               sessions.add(new Session(line[0]));
            }
         }
      } catch (Exception var4) {
      }

      this.ss.session();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      double x = 505.0D;
      double y = 60.0D + this.mouseX1;
      Iterator var9 = sessions.iterator();

      while(var9.hasNext()) {
         Session s = (Session)var9.next();
         if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, (float)y, 90.0F, 30.0F) && mouseButton == 1) {
            sessions.remove(s);
            break;
         }

         if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, (float)y, 90.0F, 30.0F) && mouseButton == 0) {
            this.ss = s;
            s.session();
         }

         if (x >= (double)(sr.getScaledWidth() - 200)) {
            x = 505.0D;
            y += 40.0D;
         } else {
            x += 100.0D;
         }
      }

      if (RenderUtil.isHovered((float)mouseX, (float)mouseY, 50.0F, 100.0F, 110.0F, 10.0F)) {
         this.login = !this.login;
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      if (keyCode == 28) {
         Session s;
         if (this.loginString.length() > 0) {
            s = new Session(this.loginString);
            this.ss = s;
            sessions.add(s);
            this.loginString = "";
            this.login = false;
         } else {
            s = new Session(RandomStringUtils.randomAlphabetic(8));
            this.ss = s;
            sessions.add(s);
            this.loginString = "";
            this.login = false;
         }
      }

      if (this.login) {
         if (keyCode == 14 && this.loginString.length() > 0) {
            this.loginString = this.loginString.substring(0, this.loginString.length() - 1);
         }

         this.loginString = this.loginString + ChatAllowedCharacters.filterAllowedCharacters(String.valueOf(typedChar));
      }

   }

   static {
      file = new File(Minecraft.getMinecraft().gameDir, "evolution" + File.separator + "alts.txt");
   }
}
