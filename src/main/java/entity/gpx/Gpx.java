package entity.gpx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "gpx", namespace = "http://www.topografix.com/GPX/1/1")
public class Gpx {
  public Metadata metadata;
  public List<Trk> trk;
  @JacksonXmlProperty(isAttribute = true)
  public String creator = "sac2gpx";
  @JacksonXmlProperty(isAttribute = true)
  public String version = "1.1";

  public static class Metadata {
    public String name;
    public String desc;
  }

  public static class Trk {
    public List<Trkseg> trkseg;
  }

  public static class Trkseg {
    public List<Trkpt> trkpt;
  }

  public static class Trkpt {
    @JacksonXmlProperty(isAttribute = true)
    public Double lat, lon;
    public Double ele = 500d;
    public String time = "2020-05-11T19:10:32.769Z";
    public Integer sat = 1;
    public Double speed = 0d;
  }
}
