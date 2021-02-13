package entity;

import java.util.List;

public class SchweizMobil {
  public List<Feature> features;

  public static class Feature {
    public String type;
    public Integer id;
    public Geometry geometry;
    public Properties properties;
  }

  public static class Geometry {
    public String type;
    public List<List<List<Double>>> coordinates;
  }

  public static class Properties {
    public String type;
    public String title;
    public String description;
    public String abstract_;
  }
}
