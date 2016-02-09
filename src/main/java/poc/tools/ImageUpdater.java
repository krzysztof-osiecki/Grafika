package poc.tools;


import java.awt.image.BufferedImage;

public interface ImageUpdater {
  void revertImage();

  void updateImage();

  void repaint();

  BufferedImage getOriginalImage();

  BufferedImage getWorkImage();

  void setWorkImage(BufferedImage image);

  void setOriginalImage(BufferedImage image);
}
