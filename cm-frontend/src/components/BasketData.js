export function createBasketData() {

    class BasketData {
        constructor(orderLines = {}, orderLinesCount = 0) {
            this.orderLines = orderLines;
            this.orderLinesCount = orderLinesCount;
        }

        isEmpty() {
            return this.orderLinesCount === 0;
        }

        changeBasket(productId, quantity) {
            let newOrderLines = {...this.orderLines};
            let newOrderLinesCount = this.orderLinesCount;
            if (productId in newOrderLines) {
                if (quantity > 0) {
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
    }

    return new BasketData();
}