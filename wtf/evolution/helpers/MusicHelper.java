/* Decompiler 8ms, total 301ms, lines 29 */
package wtf.evolution.helpers;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class MusicHelper {
   public static void playSound(String url, float volume) {
      (new Thread(() -> {
         try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = MusicHelper.class.getResourceAsStream("/assets/minecraft/" + url);
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(inputStream);
            FloatControl gainControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
            clip.start();
         } catch (Exception var6) {
            var6.printStackTrace();
         }

      })).start();
   }
}
