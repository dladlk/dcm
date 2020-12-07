package dk.erst.cm.api.load;

import org.modelmapper.ModelMapper;

public class MapperService {

	private ModelMapper modelMapper;

	public MapperService() {
		modelMapper = new ModelMapper();
	}

	public dk.erst.cm.xml.ubl20.model.Catalogue convert(dk.erst.cm.xml.ubl21.model.Catalogue c21) {
		// ModelMapper modelMapper = new ModelMapper();
		// TypeMap<dk.erst.cm.xml.ubl21.model.Catalogue, dk.erst.cm.xml.ubl20.model.Catalogue> catalogueTypeMap = modelMapper.createTypeMap(dk.erst.cm.xml.ubl21.model.Catalogue.class, dk.erst.cm.xml.ubl20.model.Catalogue.class);
		//
		// Converter<String, String> test = ctx -> ctx.getSource() + "TEST";
		// catalogueTypeMap.addMappings(mapper -> mapper.using(test).map(dk.erst.cm.xml.ubl21.model.Catalogue::getActionCode, dk.erst.cm.xml.ubl20.model.Catalogue::setActionCode));

		// dump(catalogueTypeMap.getMappings());
		//
		// TypeMap<Catalogue, dk.erst.cm.xml.ubl20.model.Catalogue> addMapping = catalogueTypeMap.addMapping(src -> src.getActionCode(), (dst, v) -> dst.setActionCode(v + "TEST"));
		// System.out.println(addMapping);
		// System.out.println(addMapping.hashCode());
		//
		// dump(catalogueTypeMap.getMappings());

		// catalogueTypeMap.addMapping(src -> src.getProviderParty().getEndpointID().getSchemeId(), (dst, v) -> dst.getProviderParty().getEndpointID().setSchemeId(convertICD((String) v)));
		// Converter<String, String> test2 = ctx -> convertICD(ctx.getSource());
		// TypeMap<dk.erst.cm.xml.ubl21.model.SchemeID, dk.erst.cm.xml.ubl20.model.SchemeID> typeMap = modelMapper.createTypeMap(dk.erst.cm.xml.ubl21.model.SchemeID.class, dk.erst.cm.xml.ubl20.model.SchemeID.class);
		// typeMap.addMappings(mapper -> mapper.using(test2).map(dk.erst.cm.xml.ubl21.model.SchemeID::getSchemeId, dk.erst.cm.xml.ubl20.model.SchemeID::setSchemeId));

		// typeMap.setPostConverter(new Converter<dk.erst.cm.xml.ubl21.model.Party, dk.erst.cm.xml.ubl20.model.Party>() {
		// @Override
		// public dk.erst.cm.xml.ubl20.model.Party convert(MappingContext<dk.erst.cm.xml.ubl21.model.Party, dk.erst.cm.xml.ubl20.model.Party> context) {
		// context.getDestination().getEndpointID().setSchemeId(convertICD(context.getSource().getEndpointID().getSchemeId()));
		// return context.getDestination();
		// }
		// });

		dk.erst.cm.xml.ubl20.model.Catalogue c20 = modelMapper.map(c21, dk.erst.cm.xml.ubl20.model.Catalogue.class);
		// c20.setUblVersionID("2.0");
		// c20.getProfileID().setId("Catalogue-CatAdv-1.0");
		// c20.getProfileID().setSchemeAgencyId("320");
		// c20.getProfileID().setSchemeId("urn:oioubl:id:profileid-1.2");
		return c20;
	}

	public dk.erst.cm.xml.ubl20.model.CatalogueLine convert(dk.erst.cm.xml.ubl21.model.CatalogueLine c21) {
		dk.erst.cm.xml.ubl20.model.CatalogueLine c20 = modelMapper.map(c21, dk.erst.cm.xml.ubl20.model.CatalogueLine.class);
		return c20;
	}
}
