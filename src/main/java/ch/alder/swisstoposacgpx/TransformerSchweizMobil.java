package ch.alder.swisstoposacgpx;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SchweizMobil;
import entity.gpx.Gpx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransformerSchweizMobil {
  static Gpx convertSchweizMobil(String infile) throws IOException {
    SchweizMobil schweizMobil = loadSchweizMobil(infile);
    dump(schweizMobil);
    return transform(schweizMobil);
  }

  private static SchweizMobil loadSchweizMobil(String infile) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    File file = new File(infile);
    SchweizMobil schweizMobil = mapper.readValue(file, SchweizMobil.class);

    if (schweizMobil.features == null)
      throw new InvalidInputFileException("Not a SchweizMobil file");
    return schweizMobil;
  }

  private static Gpx transform(SchweizMobil schweizMobil) {
    List<Gpx.Trkseg> trksegList =
            schweizMobil.features.stream()
                    .flatMap(feature -> feature.geometry.coordinates.stream())
                    .map(
                            coordinates -> {
                              Gpx.Trkseg seg = new Gpx.Trkseg();
                              seg.trkpt =
                                      coordinates.stream()
                                              .map(c -> Transformer.swissToTrkpt(c.get(0), c.get(1)))
                                              .collect(Collectors.toList());
                              return seg;
                            })
                    .collect(Collectors.toList());

    Gpx gpx = new Gpx();
    Gpx.Trk trk = new Gpx.Trk();
    trk.trkseg = trksegList;
    gpx.trk = Arrays.asList(trk);
    gpx.metadata = new Gpx.Metadata();
    gpx.metadata.name = schweizMobil.features.get(0).properties.title;
    gpx.metadata.desc = schweizMobil.features.get(0).properties.description;
    return gpx;
  }

  private static void dump(SchweizMobil schweizMobil) {
    System.out.println();
    schweizMobil.features.forEach(feature -> System.out.println(feature.properties.title));
  }
}
