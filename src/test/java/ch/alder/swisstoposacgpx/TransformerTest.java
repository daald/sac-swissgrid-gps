package ch.alder.swisstoposacgpx;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class TransformerTest {
  @Test
  void fullTest_SchweizMobil1() throws IOException {
    Transformer.convert("data/Schneeschuhtouren-leicht/Hochalp Trail - rossmoos.json");
  }

  @Test
  void fullTest_Swisstopo1() throws IOException {
    Transformer.convert(
            "data/Schneeschuhtouren-leicht/Hochhamm_Rundtour_ab_Schönengrund_2021-01-27.json");
  }

  @Test
  void fullTest_Swisstopo2() throws IOException {
    Transformer.convert("data/Schneeschuhtouren-leicht/Hundwiler_Höhi_Von_Gonten_2021-01-27.json");
  }

  @Test
  void fullTest_Swisstopo3() throws IOException {
    Transformer.convert("data/Schneeschuhtouren-leicht/Hofalpli_Von_Mullern_2021-01-31.json");
  }
}
