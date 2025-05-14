package ch.alder.swisstoposacgpx;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    if (args.length != 1 || !args[0].endsWith(".json")) {
      System.err.println("Syntax: ch.alder.swisstoposacgpx.Main <routefile.json>");
      System.err.println("Output will be <routefile.gpx>");
      System.exit(1);
    }

    String inFile = args[0];

    Transformer.convert(inFile);
  }
}
