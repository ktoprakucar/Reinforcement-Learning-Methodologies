package entity;

import java.awt.image.BufferedImage;

/**
 * Created by toprak on 07-Apr-17.
 */
public class Component {
  private int xAxis;
  private int yAxis;
  private final BufferedImage image;
  private final String name;


  public Component(int xAxis, int yAxis, BufferedImage image, String name) {
    this.xAxis = xAxis;
    this.yAxis = yAxis;
    this.image = image;
    this.name = name;
  }

  public int getxAxis() {
    return xAxis;
  }

  public void setxAxis(int xAxis) {
    this.xAxis = xAxis;
  }

  public int getyAxis() {
    return yAxis;
  }

  public void setyAxis(int yAxis) {
    this.yAxis = yAxis;
  }

  public BufferedImage getImage() {
    return image;
  }

  public void moveComponent(String direction){
    if ("up".equalsIgnoreCase(direction))
      yAxis--;
    else if ("down".equalsIgnoreCase(direction))
      yAxis++;
    else if ("rigth".equalsIgnoreCase(direction))
      xAxis++;
    else if ("left".equalsIgnoreCase(direction))
      xAxis--;
  }
}
