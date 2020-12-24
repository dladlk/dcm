import { Box } from "@material-ui/core";

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
const itemManufacturerName = (item) => {
    if (item) {
        if (item.manufacturerParty && item.manufacturerParty.partyName) {
            return item.manufacturerParty.partyName.name;
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

const itemSpecificationDocumentReferenceListFilter = (item, filter) => {
    var res = [];
    if (item) {
        const isdrl = item.itemSpecificationDocumentReferenceList;
        if (isdrl && isdrl.length > 0) {
            for (let i = 0; i < isdrl.length; i++) {
                const isdr = isdrl[i];
                if (filter(isdr)) {
                    if (isdr.attachment && isdr.attachment.externalReference && isdr.attachment.externalReference.uri) {
                        res.push(isdr);
                    }
                }
            }
        }
    }
    return res;
}

const isPictureSpecification = (isdr) => {
    return isdr && (isdr.documentTypeCode === 'Picture' || isdr.documentTypeCode === 'PRODUCT_IMAGE');
}

const itemSpecifications = (item) => {
    return itemSpecificationDocumentReferenceListFilter(item, (isdr) => !isPictureSpecification(isdr));
}

const itemPictureURL = (item) => {
    return itemSpecificationDocumentReferenceListFilter(item, isPictureSpecification);
}

const itemCertificates = (item) => {
    if (item && item.certificateList) {
        return item.certificateList;
    }
    return [];
}
const itemAdditionalProperties = (item) => {
    if (item && item.additionalItemPropertyList) {
        return item.additionalItemPropertyList;
    }
    return [];
}

const renderItemCertificate = (cert) => {
    return (
        <span key={cert.id}>
            {cert.id} - {cert.certificateType}
        </span>
    )
}
const renderItemSpecification = (s) => {
    return (
        <div>
            {s.documentTypeCode && (<Box pr={2} display="inline" style={{ fontWeight: "bold" }}>{s.documentTypeCode}</Box>)}
            {renderUrl(s.attachment.externalReference.uri)}
        </div>
    )
}
const renderItemAdditionalProperty = (s) => {
    return (
        <div>
            {s.name && (<span style={{ fontWeight: "bold" }}>{s.name}</span>)}
            {s.nameCode && (<span style={{ fontWeight: "bold" }}>{' '}{s.nameCode.id}</span>)}
            {(s.name || s.nameCode) && (":")}
            {s.value && (<span>{' '}{s.value}</span>)}
            {s.valueQuantity && (<span>{' '}{s.valueQuantity.quantity}{' '} {s.valueQuantity.unitCode}</span>)}
        </div>
    )
}

const renderUrlListValue = (v) => { return (renderUrl(v.attachment.externalReference.uri)) };

const renderUrl = (v) => { return (<a href={v} rel="noreferrer" target="_blank">{v}</a>) };

const ItemDetailsService = {
    itemOriginCountry,
    itemUNSPSC,
    itemSellerNumber,
    itemStandardNumber,
    itemPictureURL,
    itemCertificates,
    itemSpecifications,
    itemManufacturerName,
    itemAdditionalProperties,

    renderUrl,
    renderUrlListValue,
    renderItemCertificate,
    renderItemSpecification,
    renderItemAdditionalProperty,
}

export default ItemDetailsService;