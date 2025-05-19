package ch.alder.swisstoposacgpx;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class TransformerTest {
  @ParameterizedTest
  @ValueSource(strings = {
          "data/Schneeschuhtouren-leicht/Hochalp Trail - rossmoos.json",
          "data/Schneeschuhtouren-leicht/Hochhamm_Rundtour_ab_Schönengrund_2021-01-27.json",
          "data/Schneeschuhtouren-leicht/Hundwiler_Höhi_Von_Gonten_2021-01-27.json",
          "data/Schneeschuhtouren-leicht/Hofalpli_Von_Mullern_2021-01-31.json",
          "data/schweizmobil4/2571626.json"
  })
  void fullTest(String inFile, @TempDir File outDir) throws IOException, SAXException {
    System.out.println("Infile: " + inFile);
    File outFile = new File(outDir, "outfile.gpx");
    Transformer.convert(inFile, outFile.getPath());

    try {
      ValidateXmlUtil.validateXml(outFile);
    } catch (SAXException e) {
      System.out.println(Files.readString(outFile.toPath()));
      throw e;
    }
  }
}
