import { Box } from "@material-ui/core";

const mergeProducts = (list) => {
    let merged = null;
    let mergedItem = null;
    if (list && list.length > 1) {
        for (let index = 0; index < list.length; index++) {
            let listElement = list[index];
            if (listElement.document.item) {
                let item = listElement.document.item;
                if (!merged) {
                    merged = listElement;
                    merged._source = [merged.productCatalogId];
                    mergedItem = item;

                    const baseSource = {
                        code: merged.productCatalogId,
                        index: 0
                    };
                    mergedItem.name = mergeString(baseSource, null, mergedItem.name, null);
                    mergedItem.descriptionList = mergeStringList(baseSource, null, mergedItem.descriptionList, null);
                    mergedItem.itemSpecificationDocumentReferenceList = mergeObjectList(baseSource, null, mergedItem.itemSpecificationDocumentReferenceList, null);

                    continue;
                }
                merged._source.push(listElement.productCatalogId);
                let curIndex = merged._source.length - 1;

                const curSource = {
                    code: listElement.productCatalogId,
                    index: curIndex
                }
                mergedItem.name = mergeString(null, curSource, mergedItem.name, item.name);
                mergedItem.descriptionList = mergeStringList(null, curSource, mergedItem.descriptionList, item.descriptionList);
                mergedItem.itemSpecificationDocumentReferenceList = mergeObjectList(null, curSource, mergedItem.itemSpecificationDocumentReferenceList, item.itemSpecificationDocumentReferenceList);
            }

        }
    } else {
        merged = list[0];
    }
    return merged;
}

const mergeString = (baseSource, curSource, baseList, e) => {
    baseList = Array.isArray(baseList) ? baseList : baseList ? [baseList] : []
    if (baseSource)
        for (let i = 0; i < baseList.length; i++) {
            let e = baseList[i];
            if (!e._source) {
                baseList[i] = {
                    value: e,
                    _source: baseSource
                }
            }
        }
    if (e) {
        baseList.push(
            {
                value: e,
                _source: curSource
            }
        )
    }
    return baseList;
}
const mergeStringList = (baseSource, curSource, baseList, list) => {
    baseList = baseList || [];
    list = list || [];
    if (baseSource)
        for (let i = 0; i < baseList.length; i++) {
            let e = baseList[i];
            if (!e._source) {
                baseList[i] = {
                    value: e,
                    _source: baseSource
                }
            }
        }
    for (const e of list) {
        baseList.push(
            {
                value: e,
                _source: curSource
            }
        )
    }
    return baseList;
}
const mergeObjectList = (baseSource, curSource, baseList, list) => {
    baseList = baseList || [];
    list = list || [];
    if (baseSource)
        for (const e of baseList) {
            if (!e._source) {
                e._source = baseSource;
            }
        }
    for (const e of list) {
        e._source = curSource;
        baseList.push(e)
    }
    return baseList;
}

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
            {s.nameCode && (<span style={{ fontWeight: "bold" }}>{' '}{s.nameCode}</span>)}
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

    mergeProducts,
}

export default ItemDetailsService;