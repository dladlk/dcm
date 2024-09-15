package dk.erst.cm.api.order.data;

import lombok.Data;

@Data
public class CustomerOrderData {

	private Company buyerCompany;
	private Contact buyerContact;

	@Data
	public static class Contact {
		private String personName;
		private String email;
		private String telephone;
	}

	@Data
	public static class Company {
		private String registrationName;
		private String legalIdentifier;
		private String partyIdentifier;
	}

}
