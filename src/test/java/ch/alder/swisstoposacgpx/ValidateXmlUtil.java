package ch.alder.swisstoposacgpx;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ValidateXmlUtil {
  static void validateXml(File xmlFile) throws IOException, SAXException {
    URL schemaFile = new URL("https://www.topografix.com/GPX/1/1/gpx-strict.xsd");
    validateXml(schemaFile, new StreamSource(xmlFile));
  }

  static void validateXml(URL schemaFile, Source xmlFile) throws IOException, SAXException {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    Schema schema = schemaFactory.newSchema(schemaFile);
    Validator validator = schema.newValidator();
    validator.validate(xmlFile);
    System.out.println(xmlFile.getSystemId() + " is schema-valid");
  }
}
