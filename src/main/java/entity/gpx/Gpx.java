package entity.gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "gpx")
public class Gpx {
  public List<Trk> trk;
  @XmlAttribute public String creator = "sac2gpx";
  @XmlAttribute public String version = "1.1";

  public static class Trk {
    public List<Trkseg> trkseg;
  }

  public static class Trkseg {
    public List<Trkpt> trkpt;
  }

  public static class Trkpt {
    @XmlAttribute public Double lat, lon;
    public Double ele = 500d;
    public String time = "2020-05-11T19:10:32.769Z";
    public Integer sat = 1;
    public Double speed = 0d;
  }
}
