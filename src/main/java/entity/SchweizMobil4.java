package entity;

import java.util.List;

public class SchweizMobil4 {
    public String type;
    public Integer id;
    public Geometry geometry;
    public Properties properties;

  public static class Geometry {
    public String type;
    public List<List<Double>> coordinates;
  }

  public static class Properties {
    public String name;
  }
}
