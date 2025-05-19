package entity.gpx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonRootName(value = "gpx")
public class Gpx {
  public Metadata metadata;
  @JacksonXmlElementWrapper(useWrapping = false)
  public List<Trk> trk;
  @JacksonXmlProperty(isAttribute = true)
  public String creator = "sac2gpx";
  @JacksonXmlProperty(isAttribute = true)
  public String version = "1.1";

  @JacksonXmlProperty(isAttribute = true)
  private String xmlns = "http://www.topografix.com/GPX/1/1";

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Metadata {
    public String name;
    public String desc;
    public AuthorType author;
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class AuthorType {
    public String name;
  }

  public static class Trk {
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Trkseg> trkseg;
  }

  public static class Trkseg {
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Trkpt> trkpt;
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Trkpt {
    @JacksonXmlProperty(isAttribute = true)
    public Double lat, lon;
    public Double ele;
    public String time;
    public Integer sat;
  }
}
