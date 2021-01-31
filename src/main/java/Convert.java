import ch.swisstopo.ApproxSwissProj;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Convert {
  public static void main2(String[] args) throws IOException, JAXBException {
    if (args.length != 1 || args[0].endsWith(".json")) {
      System.err.println("Syntax: Convert <routefile.json>");
      System.err.println("Output will be <routefile.gpx>");
      System.exit(1);
    }

    String inFile = args[0];

    convert(inFile);
  }

  public static void main(String[] args) throws IOException, JAXBException {
    convert("data/Schneeschuhtouren-leicht/Hochhamm_Rundtour_ab_Schönengrund_2021-01-27.json");
    convert("data/Schneeschuhtouren-leicht/Hundwiler_Höhi_Von_Gonten_2021-01-27.json");
    convert("data/Schneeschuhtouren-leicht/Hofalpli_Von_Mullern_2021-01-31.json");
  }

  private static void convert(String inFile) throws IOException, JAXBException {
    String outFile = inFile.substring(0, inFile.length() - 5) + ".gpx";
    convert(inFile, outFile);
  }

  private static void convert(String infile, String outFile) throws IOException, JAXBException {
    Swisstopo swisstopo = load(infile);
    dump(swisstopo);
    Gpx gpx = transform(swisstopo);
    save(outFile, gpx);
  }

  private static Gpx transform(Swisstopo swisstopo) {
    List<Gpx.Trkseg> trksegList =
        swisstopo.segments.stream()
            .map(
                s -> {
                  Gpx.Trkseg seg = new Gpx.Trkseg();
                  seg.trkpt =
                      s.geom.coordinates.stream()
                          .map(
                              c -> {
                                Gpx.Trkpt trkpt = new Gpx.Trkpt();
                                Collections.sort(c);
                                double north = c.get(0) - 1000000;
                                double ast = c.get(1) - 2000000;
                                if (north < 0 || north > 1000000 || ast < 0 || ast > 1000000)
                                  throw new NumberFormatException();
                                double[] wgs = ApproxSwissProj.LV03toWGS84(ast, north);
                                trkpt.lat = wgs[0];
                                trkpt.lon = wgs[1];
                                return trkpt;
                              })
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

  private static Gpx.Trkpt trkpt() {
    Gpx.Trkpt trkpt = new Gpx.Trkpt();
    trkpt.lat = 123d;
    trkpt.lon = 123d;
    return trkpt;
  }

  private static void save(String outFile, Gpx gpx) throws JAXBException, FileNotFoundException {
    JAXBContext contextObj = JAXBContext.newInstance(Gpx.class);

    Marshaller marshallerObj = contextObj.createMarshaller();
    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    marshallerObj.marshal(gpx, new FileOutputStream(outFile));
  }

  private static Swisstopo load(String infile) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    //    mapper.findAndRegisterModules();

    File file = new File(infile);
    Swisstopo swisstopo = mapper.readValue(file, Swisstopo.class);
    return swisstopo;
  }
}
