package ch.alder.swisstoposacgpx;

import ch.swisstopo.ApproxSwissProj;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import entity.gpx.Gpx;

import java.io.File;
import java.io.IOException;

public class Transformer {
  public static void convert(String inFile) throws IOException {
    String outFile = inFile.substring(0, inFile.length() - 5) + ".gpx";
    Gpx gpx;
    try {
      gpx = TransformerSchweizMobil.convertSchweizMobil(inFile);
    } catch (InvalidInputFileException e) {
      try {
        gpx = TransformerSwisstopo.convertSwisstopo(inFile);
      } catch (InvalidInputFileException e2) {
        try {
          gpx = TransformerSchweizMobil4.convertSchweizMobil4(inFile);
        } catch (InvalidInputFileException e3) {
          e.printStackTrace();
          e2.printStackTrace();
          e3.printStackTrace();
          System.exit(1);
          return;
        }
      }
    }
    save(outFile, gpx);
  }

  static Gpx.Trkpt swissToTrkpt(Double east, Double north) {
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

  private static void save(String outFile, Gpx gpx) throws IOException {
    XmlMapper xm = new XmlMapper();

    xm.writeValue(new File(outFile), gpx);
  }
}
