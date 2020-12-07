package dk.erst.cm.api.load;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CATALOGUE;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import dk.erst.cm.api.load.handler.CatalogProducer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public class PeppolExportService {

	private Marshaller headMarshaller;
	private Marshaller lineMarshaller;

	public PeppolExportService() throws JAXBException {
		headMarshaller = JAXBContext.newInstance(Catalogue.class).createMarshaller();
		headMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
		if (true) {
			headMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			headMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new UblNamespacePrefixMapper());
		}

		lineMarshaller = JAXBContext.newInstance(CatalogueLine.class).createMarshaller();
		lineMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
	}

	public void marshallLine(CatalogueLine line, OutputStream out) throws JAXBException {
		lineMarshaller.marshal(line, out);
	}

	public void marshallHead(Catalogue head, OutputStream out) throws JAXBException {
		headMarshaller.marshal(head, out);
	}

	public void export(CatalogProducer<Catalogue, CatalogueLine> catalogProducer, OutputStream out) throws JAXBException {
		export(catalogProducer, out, false);
	}

	public void export(CatalogProducer<Catalogue, CatalogueLine> catalogProducer, OutputStream out, boolean nice) throws JAXBException {
		Catalogue head = catalogProducer.produceHead();
		List<CatalogueLine> lineList = head.getLineList();
		try {
			head.setLineList(new ArrayList<CatalogueLine>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Iterator<CatalogueLine> iterator() {
					return catalogProducer.lineIterator();
				}
			});
			marshallHead(head, out);
		} finally {
			head.setLineList(lineList);
		}
	}

	private static class UblNamespacePrefixMapper extends NamespacePrefixMapper {
		/**
		 * Added just to get nice namespace aliases, equal to used in tests
		 */
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
