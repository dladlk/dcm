
const itemOriginCountry = (item) => {
    if (item && item.originCountry) {
        return item.originCountry.identificationCode;
    }
    return null;
}

const itemUNSPSC = (item) => {
    if (item && item.commodityClassificationList) {
        if (item.commodityClassificationList.length > 0) {
            let code = item.commodityClassificationList[0];
            if (code && code.itemClassificationCode) {
                return code.itemClassificationCode.value;
            }
        }
    }
    return null;
}
const itemSellerNumber = (item) => {
    if (item) {
        if (item.sellersItemIdentification) {
            return item.sellersItemIdentification.id;
        }
    }
    return null;
}
const itemStandardNumber = (item) => {
    if (item) {
        if (item.standardItemIdentification && item.standardItemIdentification.id) {
            return item.standardItemIdentification.id.id;
        }
    }
    return null;
}
const itemPictureURL = (item) => {
    if (item) {
        if (item.itemSpecificationDocumentReferenceList) {
            if (item.itemSpecificationDocumentReferenceList.length > 0) {
                const isdr = item.itemSpecificationDocumentReferenceList[0];
                if (isdr.externalReference && isdr.externalReference.uri) {
                    return isdr.externalReference.uri;

                }
            }
        }
    }
    return null;
}

const ItemDetailsService = {
    itemOriginCountry,
    itemUNSPSC,
    itemSellerNumber,
    itemStandardNumber,
    itemPictureURL,
}

export default ItemDetailsService;