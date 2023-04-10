package ch.alder.swisstoposacgpx;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Swisstopo;
import entity.gpx.Gpx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransformerSwisstopo {
  static Gpx convertSwisstopo(String infile) throws IOException {
    Swisstopo swisstopo = loadSwisstopo(infile);
    dump(swisstopo);
    return transform(swisstopo);
  }

  private static Swisstopo loadSwisstopo(String infile) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    File file = new File(infile);
    Swisstopo swisstopo = mapper.readValue(file, Swisstopo.class);

    if (swisstopo.segments == null) throw new InvalidInputFileException("Not a Swisstopo file");
    return swisstopo;
  }

  private static Gpx transform(Swisstopo swisstopo) {
    List<Gpx.Trkseg> trksegList =
            swisstopo.segments.stream()
                    .map(
                            s -> {
                              Gpx.Trkseg seg = new Gpx.Trkseg();
                              seg.trkpt =
                                      s.geom.coordinates.stream()
                                              .map(c -> Transformer.swissToTrkpt(c.get(1), c.get(0)))
                                              .collect(Collectors.toList());
                              return seg;
                            })
                    .collect(Collectors.toList());

    Gpx gpx = new Gpx();
    Gpx.Trk trk = new Gpx.Trk();
    trk.trkseg = trksegList;
    gpx.trk = Arrays.asList(trk);
    return gpx;
  }

  private static void dump(Swisstopo swisstopo) {
    System.out.println();
    swisstopo.segments.forEach(s -> System.out.println(s.title));
  }

}
