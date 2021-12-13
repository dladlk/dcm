export const copyTextToClipboard = (value, postCopyFunction) => {
    try {
        navigator.clipboard.writeText(value).then(() => {
            if (postCopyFunction) {
                postCopyFunction();
            }
        });
    } catch {
    }
}

export function copyCurrentUrlToClipboard(postCopyFunction) {
    copyTextToClipboard(window.location.href, postCopyFunction);
}

const ROOT_CONTEXT = '/dcm/';

export function copySubUrlToClipboard(subPath, postCopyFunction) {
    const currentUrl = window.location.href;
    const i = currentUrl.indexOf(ROOT_CONTEXT);
    const copyPath = currentUrl.substring(0, i + ROOT_CONTEXT.length - 1) + subPath;
    copyTextToClipboard(copyPath, postCopyFunction);
}

