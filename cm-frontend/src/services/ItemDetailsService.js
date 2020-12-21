
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
        const isdrl = item.itemSpecificationDocumentReferenceList;
        if (isdrl && isdrl.length > 0) {
            const isdr = isdrl[0];
            if (isdr.attachment && isdr.attachment.externalReference && isdr.attachment.externalReference.uri) {
                return isdr.attachment.externalReference.uri;
            }
        }
    }
    return null;
}
const itemCertificates = (item) => {
    if (item && item.certificateList) {
        return item.certificateList;
    }
    return [];
}

const ItemDetailsService = {
    itemOriginCountry,
    itemUNSPSC,
    itemSellerNumber,
    itemStandardNumber,
    itemPictureURL,
    itemCertificates,
}

export default ItemDetailsService;