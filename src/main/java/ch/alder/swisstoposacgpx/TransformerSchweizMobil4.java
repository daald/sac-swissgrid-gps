package ch.alder.swisstoposacgpx;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SchweizMobil4;
import entity.gpx.Gpx;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TransformerSchweizMobil4 {
  static Gpx convertSchweizMobil4(String infile) throws IOException {
    SchweizMobil4 schweizMobil = loadSchweizMobil(infile);
    dump(schweizMobil);
    return transform(schweizMobil);
  }

  private static SchweizMobil4 loadSchweizMobil(String infile) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    File file = new File(infile);
    SchweizMobil4 schweizMobil = mapper.readValue(file, SchweizMobil4.class);

    if (schweizMobil.geometry == null)
      throw new InvalidInputFileException("Not a SchweizMobil4 file");
    return schweizMobil;
  }

  private static Gpx transform(SchweizMobil4 schweizMobil) {
    Gpx.Trkseg seg = new Gpx.Trkseg();
    seg.trkpt =
            schweizMobil.geometry.coordinates.stream()
                    .map(c -> Transformer.swissToTrkpt(c.get(0), c.get(1)))
                    .collect(Collectors.toList());

    Gpx gpx = new Gpx();
    Gpx.Trk trk = new Gpx.Trk();
    trk.trkseg = List.of(seg);
    gpx.trk = List.of(trk);
    gpx.metadata = new Gpx.Metadata();
    gpx.metadata.name = schweizMobil.properties.name;
    gpx.metadata.desc = "From Schweizmobil " + schweizMobil.id;
    return gpx;
  }

  private static void dump(SchweizMobil4 schweizMobil) {
    System.out.println();
    System.out.println(schweizMobil.properties.name);
  }
}
