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

                    mergeItems(baseSource, null, mergedItem, null);

                    continue;
                }
                merged._source.push(listElement.productCatalogId);
                let curIndex = merged._source.length - 1;

                const curSource = {
                    code: listElement.productCatalogId,
                    index: curIndex
                }
                mergeItems(null, curSource, mergedItem, item);
            }

        }
    } else {
        merged = list[0];
    }
    return merged;
}

const mergeItems = (baseSource, curSource, mergedItem, item) => {
    mergedItem.name = mergeString(baseSource, curSource, mergedItem.name, item?.name);
    mergedItem.sellersItemIdentification = mergeObject(baseSource, curSource, mergedItem?.sellersItemIdentification, item?.sellersItemIdentification);
    mergedItem.manufacturerParty = mergeObject(baseSource, curSource, mergedItem?.manufacturerParty, item?.manufacturerParty);
    mergedItem.descriptionList = mergeStringList(baseSource, curSource, mergedItem.descriptionList, item?.descriptionList);
    mergedItem.itemSpecificationDocumentReferenceList = mergeObjectList(baseSource, curSource, mergedItem.itemSpecificationDocumentReferenceList, item?.itemSpecificationDocumentReferenceList);
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
const mergeObject = (baseSource, curSource, baseList, e) => {
    baseList = Array.isArray(baseList) ? baseList : baseList ? [baseList] : []
    if (baseSource)
        for (let i = 0; i < baseList.length; i++) {
            let e = baseList[i];
            if (!e._source) {
                e._source = baseSource;
            }
        }
    if (e) {
        e._source = curSource;
        baseList.push(e);
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

const MergeService = {
    mergeProducts,
}

export default MergeService;