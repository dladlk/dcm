<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"

	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:ccts="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2" xmlns:sdt="urn:oasis:names:specification:ubl:schema:xsd:SpecializedDatatypes-2" exclude-result-prefixes="xs">

	<xsl:variable name="UBLVersionID" select="'2.0'" />
	<xsl:variable name="CustomizationID" select="'OIOUBL-2.01'" />
	<xsl:variable name="ProfileID" select="'urn:www.nesubl.eu:profiles:profile1:ver2.0'" />
	<xsl:variable name="ProfileID_schemeID" select="'urn:oioubl:id:profileid-1.2'" />
	<xsl:variable name="ProfileID_schemeAgencyID" select="'320'" />

	<xsl:template match="*[local-name()='Catalogue']">

		<!-- Start of CreditNote -->
		<Catalogue xmlns="urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2">

			<xsl:apply-templates select="node()">
				<xsl:with-param name="parentXpath" select="'Catalogue'" />
			</xsl:apply-templates>

		</Catalogue>
	</xsl:template>


	<xsl:template match="node()">
		<xsl:param name="parentXpath" />
		<xsl:variable name="currentXpath" select="concat($parentXpath, '/', local-name())" />
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="$currentXpath = 'Catalogue/UBLVersionID'">
					<xsl:value-of select="$UBLVersionID" />
				</xsl:when>
				<xsl:when test="$currentXpath = 'Catalogue/CustomizationID'">
					<xsl:value-of select="$CustomizationID" />
				</xsl:when>
				<xsl:when test="$currentXpath = 'Catalogue/ProfileID'">
					<xsl:attribute name="schemeAgencyID">
						<xsl:value-of select="$ProfileID_schemeAgencyID" />
					</xsl:attribute>
					<xsl:attribute name="schemeID">
						<xsl:value-of select="$ProfileID_schemeID" />
					</xsl:attribute>
					<xsl:value-of select="$ProfileID" />
				</xsl:when>
				<xsl:otherwise>
					<!-- Convert current node attributes -->
					<xsl:apply-templates select="@*">
						<xsl:with-param name="currentXpath" select="$currentXpath" />
					</xsl:apply-templates>

					<!-- Convert child nodes -->
					<xsl:apply-templates select="node()">
						<xsl:with-param name="parentXpath" select="$currentXpath" />
					</xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:param name="currentXpath" />
		<xsl:variable name="currentAttribute" select="local-name()" />
		<xsl:choose>
			<xsl:when test="$currentAttribute = 'schemeID'">
				<xsl:attribute name="schemeID">
						<xsl:call-template name="convertSchemeID" />
					</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="convertSchemeID">
		<xsl:variable name="OIOUBLids" select="',DUNS,GLN,IBAN,ISO 6523,ZZZ,DK:CPR,DK:CVR,DK:P,DK:SE,DK:TELEFON,FI:ORGNR,IS:KT,IS:VSKNR,NO:EFO,NO:NOBB,NO:NODI,NO:ORGNR,NO:VAT,SE:ORGNR,SE:VAT,FR:SIRET,FI:OVT,IT:FTI,IT:SIA,IT:SECETI,IT:VAT,IT:CF,HU:VAT,EU:VAT,EU:REID,AT:VAT,AT:GOV,AT:CID,IS:KT,AT:KUR,ES:VAT,IT:IPA,AD:VAT,AL:VAT,BA:VAT,BE:VAT,BG:VAT,CH:VAT,CY:VAT,CZ:VAT,DE:VAT,EE:VAT,GB:VAT,GR:VAT,HR:VAT,IE:VAT,LI:VAT,LT:VAT,LU:VAT,LV:VAT,MC:VAT,ME:VAT,MK:VAT,MT:VAT,NL:VAT,PL:VAT,PT:VAT,RO:VAT,RS:VAT,SI:VAT,SK:VAT,SM:VAT,TR:VAT,VA:VAT,'"/>
		<xsl:variable name="t1" select="."/>
			<xsl:choose>
				<xsl:when test="$t1 = '0002'">FR:SIRENE</xsl:when>
				<xsl:when test="$t1 = '0007'">SE:ORGNR</xsl:when>
				<xsl:when test="$t1 = '0009'">FR:SIRET</xsl:when>
				<xsl:when test="$t1 = '0037'">FI:OVT</xsl:when>
				<xsl:when test="$t1 = '0060'">DUNS</xsl:when>
				<xsl:when test="$t1 = '0088'">GLN</xsl:when>
				<xsl:when test="$t1 = '0096'">DK:P</xsl:when>
				<xsl:when test="$t1 = '0097'">IT:FTI</xsl:when>
				<xsl:when test="$t1 = '0106'">NL:KVK</xsl:when>
				<xsl:when test="$t1 = '0130'">EU:NAL</xsl:when>
				<xsl:when test="$t1 = '0135'">IT:SIA</xsl:when>
				<xsl:when test="$t1 = '0142'">IT:SECETI</xsl:when>
				<xsl:when test="$t1 = '0151'">AU:ABN</xsl:when>
				<xsl:when test="$t1 = '0183'">CH:UIDB</xsl:when>
				<xsl:when test="$t1 = '0184'">DK:CVR</xsl:when>
				<xsl:when test="$t1 = '0190'">NL:OINO</xsl:when>
				<xsl:when test="$t1 = '0191'">EE:CC</xsl:when>
				<xsl:when test="$t1 = '0192'">NO:ORGNR</xsl:when>
				<xsl:when test="$t1 = '0193'">UBLBE</xsl:when>
				<xsl:when test="$t1 = '0195'">SG:UEN</xsl:when>
				<xsl:when test="$t1 = '0196'">IS:KT</xsl:when>
				<xsl:when test="$t1 = '0198'">DK:SE</xsl:when>
				<xsl:when test="$t1 = '0199'">LEI</xsl:when>
				<xsl:when test="$t1 = '0200'">LT:LEC</xsl:when>
				<xsl:when test="$t1 = '0201'">IT:CUUO</xsl:when>
				<xsl:when test="$t1 = '0204'">DE:LWID</xsl:when>
				<xsl:when test="$t1 = '9901'">DK:CPR</xsl:when>
				<xsl:when test="$t1 = '9902'">DK:CVR</xsl:when>
				<xsl:when test="$t1 = '9904'">DK:SE</xsl:when>
				<xsl:when test="$t1 = '9905'">DK:VANS</xsl:when>
				<xsl:when test="$t1 = '9906'">IT:VAT</xsl:when>
				<xsl:when test="$t1 = '9907'">IT:CF</xsl:when>
				<xsl:when test="$t1 = '9908'">NO:ORGNR</xsl:when>
				<xsl:when test="$t1 = '9910'">HU:VAT</xsl:when>
				<xsl:when test="$t1 = '9913'">EU:REID</xsl:when>
				<xsl:when test="$t1 = '9914'">AT:VAT</xsl:when>
				<xsl:when test="$t1 = '9915'">AT:GOV</xsl:when>
				<xsl:when test="$t1 = '9918'">IBAN</xsl:when>
				<xsl:when test="$t1 = '9919'">AT:KUR</xsl:when>
				<xsl:when test="$t1 = '9920'">ES:VAT</xsl:when>
				<xsl:when test="$t1 = '9922'">AD:VAT</xsl:when>
				<xsl:when test="$t1 = '9923'">AL:VAT</xsl:when>
				<xsl:when test="$t1 = '9924'">BA:VAT</xsl:when>
				<xsl:when test="$t1 = '9925'">BE:VAT</xsl:when>
				<xsl:when test="$t1 = '9926'">BG:VAT</xsl:when>
				<xsl:when test="$t1 = '9927'">CH:VAT</xsl:when>
				<xsl:when test="$t1 = '9928'">CY:VAT</xsl:when>
				<xsl:when test="$t1 = '9929'">CZ:VAT</xsl:when>
				<xsl:when test="$t1 = '9930'">DE:VAT</xsl:when>
				<xsl:when test="$t1 = '9931'">EE:VAT</xsl:when>
				<xsl:when test="$t1 = '9932'">GB:VAT</xsl:when>
				<xsl:when test="$t1 = '9933'">GR:VAT</xsl:when>
				<xsl:when test="$t1 = '9934'">HR:VAT</xsl:when>
				<xsl:when test="$t1 = '9935'">IE:VAT</xsl:when>
				<xsl:when test="$t1 = '9936'">LI:VAT</xsl:when>
				<xsl:when test="$t1 = '9937'">LT:VAT</xsl:when>
				<xsl:when test="$t1 = '9938'">LU:VAT</xsl:when>
				<xsl:when test="$t1 = '9939'">LV:VAT</xsl:when>
				<xsl:when test="$t1 = '9940'">MC:VAT</xsl:when>
				<xsl:when test="$t1 = '9941'">ME:VAT</xsl:when>
				<xsl:when test="$t1 = '9942'">MK:VAT</xsl:when>
				<xsl:when test="$t1 = '9943'">MT:VAT</xsl:when>
				<xsl:when test="$t1 = '9944'">NL:VAT</xsl:when>
				<xsl:when test="$t1 = '9945'">PL:VAT</xsl:when>
				<xsl:when test="$t1 = '9946'">PT:VAT</xsl:when>
				<xsl:when test="$t1 = '9947'">RO:VAT</xsl:when>
				<xsl:when test="$t1 = '9948'">RS:VAT</xsl:when>
				<xsl:when test="$t1 = '9949'">SI:VAT</xsl:when>
				<xsl:when test="$t1 = '9950'">SK:VAT</xsl:when>
				<xsl:when test="$t1 = '9951'">SM:VAT</xsl:when>
				<xsl:when test="$t1 = '9952'">TR:VAT</xsl:when>
				<xsl:when test="$t1 = '9953'">VA:VAT</xsl:when>
				<xsl:when test="$t1 = '9955'">SE:VAT</xsl:when>
				<xsl:when test="$t1 = '9956'">BE:CBE</xsl:when>
				<xsl:when test="$t1 = '9957'">FR:VAT</xsl:when>
			
				<xsl:otherwise><xsl:value-of select="'ZZZ'"/></xsl:otherwise>
			</xsl:choose>
	</xsl:template>

</xsl:stylesheet>