package dk.erst.cm.api.load;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CATALOGUE;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import dk.erst.cm.api.load.handler.CatalogProducer;
import dk.erst.cm.xml.ubl21.model.Catalogue;

public class PeppolExportService {

	public void export(CatalogProducer catalogProducer, OutputStream out) throws JAXBException {
		export(catalogProducer, out, false);
	}

	public void export(CatalogProducer catalogProducer, OutputStream out, boolean nice) throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(Catalogue.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
		if (nice) {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new UblNamespacePrefixMapper());
		}
		marshaller.marshal(catalogProducer.produceHead(), out);
	}

	private static class UblNamespacePrefixMapper extends NamespacePrefixMapper {

		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
			if (CATALOGUE.equals(namespaceUri)) {
				return "";
			} else if (CAC.equals(namespaceUri)) {
				return "cac";
			} else if (CBC.equals(namespaceUri)) {
				return "cbc";
			}
			return suggestion;
		}

		@Override
		public String[] getPreDeclaredNamespaceUris() {
			return new String[] { CATALOGUE, CAC, CBC };
		}

	}
}
