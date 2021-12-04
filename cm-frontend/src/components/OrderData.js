const _orderData = createOrderData();

export const getOrderData = () => _orderData;

function createOrderData() {
    return {
        buyerCompany: {
            registrationName: "My Company ApS",
            legalIdentifier: "DK11223344",
            partyIdentifier: "7300010000001",
        },
        buyerContact: {
            personName: "John Doe",
            email: "unexisting@email.com",
            telephone: "+45 11223344",
        },
    }
}

function xcreateOrderData() {

    class Company {
        constructor() {
            this.registrationName = null;
            this.legalIdentifier = null;
            this.partyIdentifier = null;
        }

        setDefault() {
            this.registrationName = "My Company ApS";
            this.legalIdentifier = "DK11223344";
            this.partyIdentifier = "7300010000001";
        }
    }

    class Contact {
        constructor() {
            this.personName = null;
            this.email = null;
            this.telephone = null;
        }

        setDefault() {
            this.personName = "John Doe";
            this.email = "unexisting@email.com";
            this.telephone = "+45 11223344";
        }
    }

    class OrderData {
        constructor() {
            this.buyerCompany = new Company();
            this.buyerCompany.setDefault();
            this.buyerContact = new Contact();
            this.buyerContact.setDefault();
        }

        isEmpty() {
            // TODO: Implement empty validation
            return false;
        }
    }

    return new OrderData();
}