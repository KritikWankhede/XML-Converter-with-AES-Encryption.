package ProjectIntern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class TextToXMLConverter {

    private File outputFile;
    private boolean isConversionSuccessful;

    public void convertTextToXML(File inputFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();

            String xmlString = sb.toString();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("TextData");
            doc.appendChild(rootElement);
            rootElement.setTextContent(xmlString);

            // Write the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            outputFile = new File(inputFile.getParent(), inputFile.getName().replace(".txt", ".xml"));
            FileWriter writer = new FileWriter(outputFile);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            writer.close();

            isConversionSuccessful = true;
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            isConversionSuccessful = false;
        }
    }

    public File getOutputFile() {
        return outputFile;
    }

    public boolean isConversionSuccessful() {
        return isConversionSuccessful;
    }
}
