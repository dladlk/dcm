package dk.erst.cm.api.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dk.erst.cm.api.convert.XsdValidator.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlValidator {

	public boolean validateByXsd(InputStream xmlStream, String xmlFileDesc, String schemaPath) throws Exception {
		long start = System.currentTimeMillis();
		boolean res = false;

		if (log.isDebugEnabled()) {
			log.debug("Start XSD validation of file " + xmlFileDesc);
		}

		XsdValidator xsdValidator;
		try {
			xsdValidator = new XsdValidator(schemaPath);
		} catch (Exception e) {
			throw new Exception("Cannot build XsdValidator by path " + schemaPath, e);
		}

		try {
			List<Message> list = xsdValidator.validate(xmlStream);
			if (list != null && !list.isEmpty()) {
				log.warn(list.size() + " error/warning(s) found in " + xmlFileDesc);
				res = true;
				for (Message cuv : list) {
					log.warn(cuv.toString());
					if (cuv.getLevel().isError()) {
						res = false;
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("XSD validation of file " + xmlFileDesc + " found no errors/warnings");
				}
				res = true;
			}
		} catch (SAXException e) {
			log.error("Invalid XML at " + xmlFileDesc, e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Done schema validation of file " + xmlFileDesc + " with result " + (res ? "SUCCESS" : "ERROR") + " in " + (System.currentTimeMillis() - start) + " ms");
		}

		return res;
	}

	public boolean validateBySchematron(InputStream xmlStream, String xmlFileDesc, String schematronPath) {
		boolean res = false;
		long start = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Start schematron validation of " + xmlFileDesc);
		}
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new FileInputStream(schematronPath)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			transformer.transform(new StreamSource(xmlStream), new StreamResult(baos));

			byte[] result = baos.toByteArray();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(result));

			NodeList errorList = document.getElementsByTagName("Error");

			if (errorList != null && errorList.getLength() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("Found " + errorList.getLength() + " error(s):");

				for (int i = 0; i < errorList.getLength(); i++) {
					Node errorItem = errorList.item(i);
					sb.append("\r\n" + i + ")");
					for (int j = 0; j < errorItem.getChildNodes().getLength(); j++) {
						Node errorChild = errorItem.getChildNodes().item(j);
						sb.append("\r\n\t" + errorChild.getNodeName() + ": " + errorChild.getTextContent());
					}
				}
				log.warn(sb.toString());
			} else {
				if (log.isDebugEnabled()) {
					log.debug("No errors are found by schematron at file " + xmlFileDesc);
				}
				res = true;
			}

		} catch (Exception e) {
			log.error("Failed schematron validation of file " + xmlFileDesc + " with schematron " + schematronPath, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Done schematron validation of file " + xmlFileDesc + " with result " + (res ? "SUCCESS" : "ERROR") + " in " + (System.currentTimeMillis() - start) + " ms");
		}
		return res;
	}

}
