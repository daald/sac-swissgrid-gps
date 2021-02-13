package ch.alder.swisstoposacgpx;

import java.io.IOException;

public class InvalidInputFileException extends IOException {
  public InvalidInputFileException(String message) {
    super(message);
  }
}
