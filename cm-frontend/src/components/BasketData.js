export const ProductBasketStatus = {
    Empty: 'empty',
    Adding: 'adding',
    Added: 'added',
}

export function createBasketData() {

    class BasketData {
        constructor(orderLines = {}, orderLinesCount = 0) {
            this.orderLines = orderLines;
            this.orderLinesCount = orderLinesCount;
        }

        isEmpty() {
            return this.orderLinesCount === 0;
        }

        getOrderLineList() {
            return Object.keys(this.orderLines).map((productId) => {
                return {productId: productId, quantity: this.orderLines[productId]}
            });
        }

        changeBasket(productId, quantity) {
            let newOrderLines = {...this.orderLines};
            let newOrderLinesCount = this.orderLinesCount;
            if (productId in newOrderLines) {
                if (quantity !== 0) {
                    newOrderLines[productId] += quantity;
                } else {
                    delete newOrderLines[productId]
                    newOrderLinesCount--;
                }
            } else {
                newOrderLines[productId] = quantity;
                newOrderLinesCount++;
            }
            return new BasketData(newOrderLines, newOrderLinesCount);
        }

        getProductBasketStatus(productId) {
            return productId in this.orderLines ? ProductBasketStatus.Added : ProductBasketStatus.Empty;
        }
    }

    return new BasketData();
}

export function createOrderData() {

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