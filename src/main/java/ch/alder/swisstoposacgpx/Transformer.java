package ch.alder.swisstoposacgpx;

import ch.swisstopo.ApproxSwissProj;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SchweizMobil;
import entity.Swisstopo;
import entity.gpx.Gpx;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Transformer {
  public static void convert(String inFile) throws IOException, JAXBException {
    String outFile = inFile.substring(0, inFile.length() - 5) + ".gpx";
    Gpx gpx;
    try {
      gpx = convertSchweizMobil(inFile);
    } catch (InvalidInputFileException e) {
      gpx = convertSwisstopo(inFile);
    }
    save(outFile, gpx);
  }

  private static Gpx convertSwisstopo(String infile) throws IOException {
    Swisstopo swisstopo = loadSwisstopo(infile);
    dump(swisstopo);
    return transform(swisstopo);
  }

  private static Gpx convertSchweizMobil(String infile) throws IOException {
    SchweizMobil schweizMobil = loadSchweizMobil(infile);
    dump(schweizMobil);
    return transform(schweizMobil);
  }

  private static Gpx transform(Swisstopo swisstopo) {
    List<Gpx.Trkseg> trksegList =
        swisstopo.segments.stream()
            .map(
                s -> {
                  Gpx.Trkseg seg = new Gpx.Trkseg();
                  seg.trkpt =
                      s.geom.coordinates.stream()
                          .map(c -> swissToTrkpt(c.get(1), c.get(0)))
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

  private static Gpx.Trkpt swissToTrkpt(Double east, Double north) {
    if (north >= 1000000 && east >= 1000000) {
      if (north >= 2000000) {
        Double big = north;
        north = east - 1000000;
        east = big - 2000000;
      } else if (east >= 2000000) {
        north -= 1000000;
        east -= 2000000;
      } else {
        throw new NumberFormatException();
      }
    }
    if (north < 0 || north > 1000000 || east < 0 || east > 1000000)
      throw new NumberFormatException();

    double[] wgs = ApproxSwissProj.LV03toWGS84(east, north);

    Gpx.Trkpt trkpt = new Gpx.Trkpt();
    trkpt.lat = wgs[0];
    trkpt.lon = wgs[1];
    return trkpt;
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
                          .map(c -> swissToTrkpt(c.get(0), c.get(1)))
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

  private static void dump(SchweizMobil schweizMobil) {
    System.out.println();
    schweizMobil.features.forEach(feature -> System.out.println(feature.properties.title));
  }

  private static void save(String outFile, Gpx gpx) throws JAXBException, FileNotFoundException {
    JAXBContext contextObj = JAXBContext.newInstance(Gpx.class);

    Marshaller marshallerObj = contextObj.createMarshaller();
    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    marshallerObj.marshal(gpx, new FileOutputStream(outFile));
  }

  private static Swisstopo loadSwisstopo(String infile) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    File file = new File(infile);
    Swisstopo swisstopo = mapper.readValue(file, Swisstopo.class);

    if (swisstopo.segments == null) throw new InvalidInputFileException("Not a Swisstopo file");
    return swisstopo;
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
}
