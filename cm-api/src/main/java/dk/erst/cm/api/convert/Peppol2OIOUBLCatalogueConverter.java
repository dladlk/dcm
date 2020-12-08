package dk.erst.cm.api.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.erst.cm.api.convert.ModelConverter.CatalogueConverter;
import dk.erst.cm.xml.ubl21.model.AdditionalItemProperty;
import dk.erst.cm.xml.ubl21.model.Attachment;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.CommodityClassification;
import dk.erst.cm.xml.ubl21.model.DocumentReference;
import dk.erst.cm.xml.ubl21.model.Item;
import dk.erst.cm.xml.ubl21.model.ItemClassificationCode;
import dk.erst.cm.xml.ubl21.model.ItemComparison;
import dk.erst.cm.xml.ubl21.model.ItemInstance;
import dk.erst.cm.xml.ubl21.model.ListID;
import dk.erst.cm.xml.ubl21.model.NestedID;
import dk.erst.cm.xml.ubl21.model.Party;
import dk.erst.cm.xml.ubl21.model.PostalAddress;
import dk.erst.cm.xml.ubl21.model.RelatedItem;
import dk.erst.cm.xml.ubl21.model.RequiredItemLocationQuantity;
import dk.erst.cm.xml.ubl21.model.SchemeID;
import dk.erst.cm.xml.ubl21.model.UnitQuantity;

public class Peppol2OIOUBLCatalogueConverter implements CatalogueConverter {

	private String PEPPOL_SCHEME_ID = "0002,0007,0009,0037,0060,0088,0096,0097,0106,0130,0135,0142,0151,0183,0184,0190,0191,0192,0193,0195,0196,0198,0199,0200,0201,0204,9901,9902,9904,9905,9906,9907,9908,9910,9913,9914,9915,9918,9919,9920,9922,9923,9924,9925,9926,9927,9928,9929,9930,9931,9932,9933,9934,9935,9936,9937,9938,9939,9940,9941,9942,9943,9944,9945,9946,9947,9948,9949,9950,9951,9952,9953,9955,9956,9957";
	private String OIOUBL_SCHEME_ID = "FR:SIRENE,SE:ORGNR,FR:SIRET,FI:OVT,DUNS,GLN,DK:P,IT:FTI,NL:KVK,EU:NAL,IT:SIA,IT:SECETI,AU:ABN,CH:UIDB,DK:CVR,NL:OINO,EE:CC,NO:ORGNR,UBLBE,SG:UEN,IS:KT,DK:SE,LEI,LT:LEC,IT:CUUO,DE:LWID,DK:CPR,DK:CVR,DK:SE,DK:VANS,IT:VAT,IT:CF,NO:ORGNR,HU:VAT,EU:REID,AT:VAT,AT:GOV,IBAN,AT:KUR,ES:VAT,AD:VAT,AL:VAT,BA:VAT,BE:VAT,BG:VAT,CH:VAT,CY:VAT,CZ:VAT,DE:VAT,EE:VAT,GB:VAT,GR:VAT,HR:VAT,IE:VAT,LI:VAT,LT:VAT,LU:VAT,LV:VAT,MC:VAT,ME:VAT,MK:VAT,MT:VAT,NL:VAT,PL:VAT,PT:VAT,RO:VAT,RS:VAT,SI:VAT,SK:VAT,SM:VAT,TR:VAT,VA:VAT,SE:VAT,BE:CBE,FR:VAT";

	private String OIOUBL_UNITS = "04,05,08,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,40,41,43,44,45,46,47,48,53,54,56,57,58,59,60,61,62,63,64,66,69,71,72,73,74,76,77,78,80,81,84,85,87,89,90,91,92,93,94,95,96,97,98,1A,1B,1C,1D,1E,1F,1G,1H,1I,1J,1K,1L,1M,1X,2A,2B,2C,2I,2J,2K,2L,2M,2N,2P,2Q,2R,2U,2V,2W,2X,2Y,2Z,3B,3C,3E,3G,3H,3I,4A,4B,4C,4E,4G,4H,4K,4L,4M,4N,4O,4P,4Q,4R,4T,4U,4W,4X,5A,5B,5C,5E,5F,5G,5H,5I,5J,5K,5P,5Q,A1,A10,A11,A12,A13,A14,A15,A16,A17,A18,A19,A2,A20,A21,A22,A23,A24,A25,A26,A27,A28,A29,A3,A30,A31,A32,A33,A34,A35,A36,A37,A38,A39,A4,A40,A41,A42,A43,A44,A45,A47,A48,A49,A5,A50,A51,A52,A53,A54,A55,A56,A57,A58,A6,A60,A61,A62,A63,A64,A65,A66,A67,A68,A69,A7,A70,A71,A73,A74,A75,A76,A77,A78,A79,A8,A80,A81,A82,A83,A84,A85,A86,A87,A88,A89,A9,A90,A91,A93,A94,A95,A96,A97,A98,AA,AB,ACR,AD,AE,AH,AI,AJ,AK,AL,AM,AMH,AMP,ANN,AP,APZ,AQ,AR,ARE,AS,ASM,ASU,ATM,ATT,AV,AW,AY,AZ,B0,B1,B11,B12,B13,B14,B15,B16,B18,B2,B20,B21,B22,B23,B24,B25,B26,B27,B28,B29,B3,B31,B32,B33,B34,B35,B36,B37,B38,B39,B4,B40,B41,B42,B43,B44,B45,B46,B47,B48,B49,B5,B50,B51,B52,B53,B54,B55,B56,B57,B58,B59,B6,B60,B61,B62,B63,B64,B65,B66,B67,B69,B7,B70,B71,B72,B73,B74,B75,B76,B77,B78,B79,B8,B81,B83,B84,B85,B86,B87,B88,B89,B9,B90,B91,B92,B93,B94,B95,B96,B97,B98,B99,BAR,BB,BD,BE,BFT,BG,BH,BHP,BIL,BJ,BK,BL,BLD,BLL,BO,BP,BQL,BR,BT,BTU,BUA,BUI,BW,BX,BZ,C0,C1,C10,C11,C12,C13,C14,C15,C16,C17,C18,C19,C2,C20,C22,C23,C24,C25,C26,C27,C28,C29,C3,C30,C31,C32,C33,C34,C35,C36,C38,C39,C4,C40,C41,C42,C43,C44,C45,C46,C47,C48,C49,C5,C50,C51,C52,C53,C54,C55,C56,C57,C58,C59,C6,C60,C61,C62,C63,C64,C65,C66,C67,C68,C69,C7,C70,C71,C72,C73,C75,C76,C77,C78,C8,C80,C81,C82,C83,C84,C85,C86,C87,C88,C89,C9,C90,C91,C92,C93,C94,C95,C96,C97,C98,C99,CA,CCT,CDL,CEL,CEN,CG,CGM,CH,CJ,CK,CKG,CL,CLF,CLT,CMK,CMQ,CMT,CNP,CNT,CO,COU,CQ,CR,CS,CT,CTM,CU,CUR,CV,CWA,CWI,CY,CZ,D1,D10,D12,D13,D14,D15,D16,D17,D18,D19,D2,D20,D21,D22,D23,D24,D25,D26,D27,D28,D29,D30,D31,D32,D33,D34,D35,D37,D38,D39,D40,D41,D42,D43,D44,D45,D46,D47,D48,D49,D5,D50,D51,D52,D53,D54,D55,D56,D57,D58,D59,D6,D60,D61,D62,D63,D64,D65,D66,D67,D69,D7,D70,D71,D72,D73,D74,D75,D76,D77,D79,D8,D80,D81,D82,D83,D85,D86,D87,D88,D89,D9,D90,D91,D92,D93,D94,D95,D96,D97,D98,D99,DAA,DAD,DAY,DB,DC,DD,DE,DEC,DG,DI,DJ,DLT,DMK,DMQ,DMT,DN,DPC,DPR,DPT,DQ,DR,DRA,DRI,DRL,DRM,DS,DT,DTN,DU,DWT,DX,DY,DZN,DZP,E2,E3,E4,E5,EA,EB,EC,EP,EQ,EV,F1,F9,FAH,FAR,FB,FC,FD,FE,FF,FG,FH,FL,FM,FOT,FP,FR,FS,FTK,FTQ,G2,G3,G7,GB,GBQ,GC,GD,GE,GF,GFI,GGR,GH,GIA,GII,GJ,GK,GL,GLD,GLI,GLL,GM,GN,GO,GP,GQ,GRM,GRN,GRO,GRT,GT,GV,GW,GWH,GY,GZ,H1,H2,HA,HAR,HBA,HBX,HC,HD,HE,HF,HGM,HH,HI,HIU,HJ,HK,HL,HLT,HM,HMQ,HMT,HN,HO,HP,HPA,HS,HT,HTZ,HUR,HY,IA,IC,IE,IF,II,IL,IM,INH,INK,INQ,IP,IT,IU,IV,J2,JB,JE,JG,JK,JM,JO,JOU,JR,K1,K2,K3,K5,K6,KA,KB,KBA,KD,KEL,KF,KG,KGM,KGS,KHZ,KI,KJ,KJO,KL,KMH,KMK,KMQ,KNI,KNS,KNT,KO,KPA,KPH,KPO,KPP,KR,KS,KSD,KSH,KT,KTM,KTN,KUR,KVA,KVR,KVT,KW,KWH,KWT,KX,L2,LA,LBR,LBT,LC,LD,LE,LEF,LF,LH,LI,LJ,LK,LM,LN,LO,LP,LPA,LR,LS,LTN,LTR,LUM,LUX,LX,LY,M0,M1,M4,M5,M7,M9,MA,MAL,MAM,MAW,MBE,MBF,MBR,MC,MCU,MD,MF,MGM,MHZ,MIK,MIL,MIN,MIO,MIU,MK,MLD,MLT,MMK,MMQ,MMT,MON,MPA,MQ,MQH,MQS,MSK,MT,MTK,MTQ,MTR,MTS,MV,MVA,MWH,N1,N2,N3,NA,NAR,NB,NBB,NC,NCL,ND,NE,NEW,NF,NG,NH,NI,NIU,NJ,NL,NMI,NMP,NN,NPL,NPR,NPT,NQ,NR,NRL,NT,NTT,NU,NV,NX,NY,OA,OHM,ON,ONZ,OP,OT,OZ,OZA,OZI,P0,P1,P2,P3,P4,P5,P6,P7,P8,P9,PA,PAL,PB,PD,PE,PF,PG,PGL,PI,PK,PL,PM,PN,PO,PQ,PR,PS,PT,PTD,PTI,PTL,PU,PV,PW,PY,PZ,Q3,QA,QAN,QB,QD,QH,QK,QR,QT,QTD,QTI,QTL,QTR,R1,R4,R9,RA,RD,RG,RH,RK,RL,RM,RN,RO,RP,RPM,RPS,RS,RT,RU,S3,S4,S5,S6,S7,S8,SA,SAN,SCO,SCR,SD,SE,SEC,SET,SG,SHT,SIE,SK,SL,SMI,SN,SO,SP,SQ,SR,SS,SST,ST,STI,STN,SV,SW,SX,T0,T1,T3,T4,T5,T6,T7,T8,TA,TAH,TC,TD,TE,TF,TI,TJ,TK,TL,TN,TNE,TP,TPR,TQ,TQD,TR,TRL,TS,TSD,TSH,TT,TU,TV,TW,TY,U1,U2,UA,UB,UC,UD,UE,UF,UH,UM,VA,VI,VLT,VQ,VS,W2,W4,WA,WB,WCD,WE,WEB,WEE,WG,WH,WHR,WI,WM,WR,WSD,WTT,WW,X1,YDK,YDQ,YL,YRD,YT,Z1,Z2,Z3,Z4,Z5,Z6,Z8,ZP,ZZ";

	private Map<String, String> peppolToOioublSchemeIdMap;
	private Map<String, String> oioublToPeppolSchemeIdMap;

	private Set<String> oioublUnits;

	public Peppol2OIOUBLCatalogueConverter() {
		buildSchemeIdMaps();
		buildUnitMaps();
	}

	private void buildUnitMaps() {
		oioublUnits = new HashSet<String>();
		String[] units = OIOUBL_UNITS.split(",");
		for (String unit : units) {
			oioublUnits.add(unit);
		}
	}

	public void buildSchemeIdMaps() {
		peppolToOioublSchemeIdMap = new HashMap<String, String>();
		oioublToPeppolSchemeIdMap = new HashMap<String, String>();
		String peppol[] = PEPPOL_SCHEME_ID.split(",");
		String oioubl[] = OIOUBL_SCHEME_ID.split(",");
		if (peppol.length != oioubl.length) {
			throw new IllegalArgumentException("SchemeID dicts do not match");
		}
		for (int i = 0; i < peppol.length; i++) {
			String peppolCode = peppol[i];
			String oioublCode = oioubl[i];
			peppolToOioublSchemeIdMap.put(peppolCode, oioublCode);
			oioublToPeppolSchemeIdMap.put(oioublCode, peppolCode);
		}
	}

	@Override
	public Catalogue convert(Catalogue c) {
		c.setUblVersionID("2.0");
		c.setCustomizationID("OIOUBL-2.02");
		c.getProfileID().setId("Catalogue-CatAdv-1.0");
		c.getProfileID().setSchemeAgencyId("320");
		c.getProfileID().setSchemeId("urn:oioubl:id:profileid-1.2");
		c.setActionCode(null);
		c.setSourceCatalogueReference(null);

		convertParty(c.getProviderParty());
		convertParty(c.getReceiverParty());
		if (c.getContractorCustomerParty() != null) {
			convertParty(c.getContractorCustomerParty().getParty());
		}
		if (c.getSellerSupplierParty() != null) {
			convertParty(c.getSellerSupplierParty().getParty());
		}

		return c;
	}

	private void convertParty(Party p) {
		if (p != null) {
			convertSchemeID(p.getEndpointID());
			if (p.getPartyIdentification() != null) {
				convertSchemeID(p.getPartyIdentification().getId());
			}
			if (p.getPartyLegalEntity() != null) {
				convertSchemeID(p.getPartyLegalEntity().getCompanyID());
				if (p.getPartyLegalEntity().getCompanyID() != null) {
					if (!",DK:CVR,DK:CPR,ZZZ,".contains("," + p.getPartyLegalEntity().getCompanyID().getSchemeId() + ",")) {
						p.getPartyLegalEntity().getCompanyID().setSchemeId("ZZZ");
					}
				}
			}
			convertPostalAddress(p.getPostalAddress());
		}
	}

	private void convertPostalAddress(PostalAddress pa) {
		if (pa != null) {
			if (pa.getAddressFormatCode() == null) {
				ListID addressFormatCode = new ListID();

				addressFormatCode.setId("StructuredLax");
				addressFormatCode.setListID("urn:oioubl:codelist:addressformatcode-1.1");
				addressFormatCode.setListAgencyID("320");
				pa.setAddressFormatCode(addressFormatCode);

				String additionalStreetName = null;
				if (pa.getAddressLine() != null) {
					additionalStreetName = pa.getAddressLine().get(0).getLine();
				}

				if (pa.getAdditionalStreetName() != null) {
					if (additionalStreetName != null) {
						pa.setAdditionalStreetName(pa.getAdditionalStreetName() + " " + additionalStreetName);
					}
				} else {
					pa.setAdditionalStreetName(additionalStreetName);
				}
				pa.setAddressLine(null);
			}
		}
	}

	private void convertSchemeID(SchemeID schemeID) {
		if (schemeID != null) {
			String original = schemeID.getSchemeId();
			String oioubl = peppolToOioublSchemeIdMap.get(original);
			if (oioubl == null) {
				if (oioublToPeppolSchemeIdMap.containsKey(original)) {
					oioubl = original;
				}
			}
			if (oioubl == null) {
				oioubl = "ZZZ";
			}
			schemeID.setSchemeId(oioubl);
		}
	}

	@Override
	public CatalogueLine convert(CatalogueLine c) {
		if (c.getOrderableIndicator() == null) {
			c.setOrderableIndicator("true"); // [F-CAT106] Invalid OrderableIndicator. Must contain a value
			if (c.getOrderableUnit() == null) {
				c.setOrderableUnit("EA"); // [F-CAT115] When OrderableIndicator equals 'true' the element OrderableUnit must be filled out
			}
		}
		if (c.getContentUnitQuantity() == null) {
			UnitQuantity cuq = new UnitQuantity();
			cuq.setQuantity("1");
			cuq.setUnitCode("EA");
			c.setContentUnitQuantity(cuq); // [F-CAT108] Invalid ContentUnitQuantity. Must contain a value
		}
		if (c.getActionCode() == null) {
			ListID listId = new ListID();
			listId.setId("Add");
			c.setActionCode(listId); // [F-CAT111] Invalid ActionCode: ''. Must be a value from the codelist
		}
		if (c.getActionCode() != null) {
			c.getActionCode().setListAgencyID("320"); // [F-CAT103] Invalid listAgencyID. Must be '320'
			c.getActionCode().setListID("urn:oioubl:codelist:catalogueactioncode-1.1"); // [F-CAT102] Invalid listID. Must be 'urn:oioubl:codelist:catalogueactioncode-1.1'
		}
		convertUnitCode(c.getContentUnitQuantity());
		c.setOrderableUnit(convertUnitCode(c.getOrderableUnit()));
		convertUnitCode(c.getMinimumOrderQuantity());
		convertUnitCode(c.getMaximumOrderQuantity());

		if (c.getItemPrice() != null) {
			RequiredItemLocationQuantity itemPrice = c.getItemPrice();
			convertPostalAddress(itemPrice.getApplicableTerritoryAddress());

			convertUnitCode(itemPrice.getMinimumQuantity());
			convertUnitCode(itemPrice.getMaximumQuantity());

			if (itemPrice.getPrice() != null) {
				convertUnitCode(itemPrice.getPrice().getBaseQuantity());
			}
		}

		if (c.getItem() != null) {
			Item item = c.getItem();

			convertUnitCode(item.getPackQuantity());

			if (item.getAdditionalItemPropertyList() != null) {
				for (AdditionalItemProperty p : item.getAdditionalItemPropertyList()) {
					p.setNameCode(null); // XSD
					p.setValueQuantity(null); // XSD
					p.setValueQualifier(null); // XSD
				}
			}
			if (item.getItemInstanceList() != null) {
				for (ItemInstance itemInstance : item.getItemInstanceList()) {
					itemInstance.setBestBeforeDate(null); // XSD
				}
			}
			item.setCertificateList(null); // XSD
			item.setDimensionList(null); // XSD
			item.setClassifiedTaxCategory(null); // [F-CAT234] ClassifiedTaxCategory class must be excluded
			item.setOriginCountry(null); // [F-CAT241] OriginCountry class must be excluded

			if (item.getCommodityClassificationList() != null) {
				List<CommodityClassification> classificationList = item.getCommodityClassificationList();
				for (CommodityClassification commodityClassification : classificationList) {
					ItemClassificationCode icc = commodityClassification.getItemClassificationCode();
					convertItemClassificationCode(icc);
				}
			} else {
				// [F-CAT230] At least one CommodityClassification class must be present
				item.setCommodityClassificationList(new ArrayList<CommodityClassification>());
				CommodityClassification cc = new CommodityClassification();
				ItemClassificationCode icc = new ItemClassificationCode();
				icc.setValue("00000000");
				cc.setItemClassificationCode(icc);
				convertItemClassificationCode(icc);
				item.getCommodityClassificationList().add(cc);
			}

			if (item.getSellersItemIdentification() == null) {
				// [F-CAT223] One SellersItemIdentification class must be present

				// PEPPOL-T19-R012 Each item in a Catalogue line SHALL be identifiable by either "item sellers identifier" or "item standard identifier"
				// ( https://docs.peppol.eu/poacc/upgrade-3/syntax/Catalogue/cac-CatalogueLine/cac-Item/cac-SellersItemIdentification/ )
				NestedID nestedID = new NestedID();
				nestedID.setId(item.getStandardItemIdentification().getId().getId());
				item.setSellersItemIdentification(nestedID);
			}

			if (item.getItemSpecificationDocumentReferenceList() != null) {
				for (DocumentReference documentReference : item.getItemSpecificationDocumentReferenceList()) {
					if (documentReference.getDocumentType() == null && documentReference.getDocumentTypeCode() == null) {
						documentReference.setDocumentType("N/A"); // [F-LIB092] Use either DocumentType or DocumentTypeCode
					}
					if (documentReference.getAttachment() != null) {
						Attachment attachment = documentReference.getAttachment();
						if (attachment.getEmbeddedDocumentBinaryObject() != null && attachment.getExternalReference() != null) {
							attachment.setExternalReference(null); // [F-LIB095] Use either EmbeddedDocumentBinaryObject or ExternalReference
						}
					}
				}
			}
		}

		convertRelatedItemList(c.getAccessoryRelatedItem());
		convertRelatedItemList(c.getComponentRelatedItem());
		convertRelatedItemList(c.getRequiredRelatedItem());
		convertItemComparison(c.getItemComparison());

		return c;
	}

	public void convertItemClassificationCode(ItemClassificationCode icc) {
		icc.setListID("UNSPSC");
		icc.setListAgencyID("113");
	}

	private void convertItemComparison(List<ItemComparison> list) {
		if (list != null) {
			for (ItemComparison i : list) {
				convertUnitCode(i.getQuantity());
			}
			if (list.size() > 1) {
				// [F-CAT113] No more than one ItemComparison class may be present
				ItemComparison itemComparison = list.get(0);
				list.clear();
				list.add(itemComparison);
			}
		}
	}

	private void convertRelatedItemList(List<RelatedItem> list) {
		if (list != null) {
			for (RelatedItem i : list) {
				convertUnitCode(i.getQuantity());
			}
		}
	}

	private void convertUnitCode(UnitQuantity unit) {
		if (unit != null) {
			unit.setUnitCode(convertUnitCode(unit.getUnitCode()));
		}
	}

	public String convertUnitCode(String unitCode) {
		if (unitCode == null) {
			return unitCode;
		}
		if (unitCode.startsWith("X")) {
			unitCode = unitCode.substring(1);
		}
		if (!oioublUnits.contains(unitCode)) {
			unitCode = "EA";
		}
		return unitCode;
	}

}