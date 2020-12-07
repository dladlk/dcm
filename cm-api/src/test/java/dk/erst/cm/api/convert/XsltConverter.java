package dk.erst.cm.api.convert;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltConverter {

	private Templates templates;

	public XsltConverter() throws TransformerException {
		templates = TransformerFactory.newInstance().newTemplates(new StreamSource(this.getClass().getResourceAsStream("/PEPPOL_BIS3_CATALOGUE_2_OIOUBL.xsl")));
	}

	public void convert(InputStream inputStream, OutputStream outputStream) throws TransformerException {
		Transformer transformer = templates.newTransformer();
		transformer.transform(new StreamSource(inputStream), new StreamResult(outputStream));
	}
}
