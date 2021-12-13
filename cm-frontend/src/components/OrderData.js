// Global state for order data - because I want not finished input of sending form to be kept between page navigations,
// so changes should be applied without any submit. But when useState on the root component was used, any changes
// led to re-render of many components. So instead local useState per input is used, current global value is passed as
// parameter and is used as local state initial value in each input.
const _orderData = createOrderData();

export const getOrderData = () => _orderData;

function createOrderData() {

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
            this.email = "some@email.com";
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