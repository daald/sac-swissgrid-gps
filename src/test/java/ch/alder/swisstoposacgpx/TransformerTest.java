package ch.alder.swisstoposacgpx;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

class TransformerTest {
  @Test
  void fullTest1() throws IOException, JAXBException {
    Transformer.convert(
        "data/Schneeschuhtouren-leicht/Hochhamm_Rundtour_ab_Schönengrund_2021-01-27.json");
  }

  @Test
  void fullTest2() throws IOException, JAXBException {
    Transformer.convert("data/Schneeschuhtouren-leicht/Hundwiler_Höhi_Von_Gonten_2021-01-27.json");
  }

  @Test
  void fullTest3() throws IOException, JAXBException {
    Transformer.convert("data/Schneeschuhtouren-leicht/Hofalpli_Von_Mullern_2021-01-31.json");
  }
}
