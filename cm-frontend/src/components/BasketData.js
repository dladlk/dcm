export function createBasketData() {

    class BasketData {
        constructor() {
            this.orderLines = {};
            this.orderLinesCount = 0;
        }

        isEmpty() {
            return this.orderLines.length === 0;
        }
    }

    return new BasketData();
}