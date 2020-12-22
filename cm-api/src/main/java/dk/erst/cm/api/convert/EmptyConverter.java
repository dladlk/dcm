package dk.erst.cm.api.convert;

import dk.erst.cm.api.convert.ModelConverter.CatalogueConverter;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public class EmptyConverter implements CatalogueConverter {

	@Override
	public CatalogueLine convert(CatalogueLine catalogueLine) {
		return catalogueLine;
	}

	@Override
	public Catalogue convert(Catalogue catalogue) {
		return catalogue;
	}

}
