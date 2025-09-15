/**
 * LinkInfo is a wrapper for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(() => {

    "use strict";

    const PROXY_HOST_REGEX = /^(?:login\.)?laneproxy\.stanford\.edu$/;
    const PROXY_LOGIN_PATH = "/login";
    const BASE_PATH = L.Model.get("base-path") || "";
    const DOCUMENT_HOSTNAME = window.location.hostname;
    const LOGIN_PATH = `${BASE_PATH}/secure/apps/proxy/credential`;
    class LinkInfo {

        /**
         * The raw anchor DOM node.
         * @private
         * @type {HTMLAnchorElement}
         */
        #node;

        /**
         * @param {HTMLAnchorElement} node The anchor element to wrap.
         */
        constructor(node) {
            this.#node = node;
        }

        /** @returns {boolean} */
        #isLocalPopup() {
            return this.#node.rel?.startsWith("popup local") ?? false;
        }

        /** @returns {string|undefined} */
        #getTitleFromImg() {
            const img = this.#node.querySelector('img[alt], img[src]');
            return img?.alt || img?.src;
        }

        /** The hostname of the link, stripping any port number. */
        get linkHost() {
            return this.#node.hostname || DOCUMENT_HOSTNAME;
        }

        /** Is the link to a local, non-proxied resource? */
        get local() {
            return this.linkHost === DOCUMENT_HOSTNAME && !this.proxyLogin;
        }

        /** The full path of the link, ensuring it starts with a '/'. */
        get path() {
            const path = this.#node.pathname || window.location.pathname;
            return path.startsWith('/') ? path : `/${path}`;
        }

        /** Is the link a proxy link? */
        get proxy() {
            return PROXY_HOST_REGEX.test(this.linkHost) && this.path === PROXY_LOGIN_PATH;
        }

        /** Is the link a proxy login link? */
        get proxyLogin() {
            return this.linkHost === DOCUMENT_HOSTNAME && this.path === LOGIN_PATH;
        }

        /** The query string of the link. */
        get query() {
            return this.local ? this.#node.search : "";
        }

        /** A descriptive title for the link, derived from various link attributes. */
        get title() {
            let title = this.#node.title
                || this.#node.alt
                || this.#getTitleFromImg()
                || this.#node.textContent
                || 'unknown';
            if (this.#isLocalPopup()) {
                title = `YUI Pop-up [local]: ${title}`;
            }
            title = title.trim().replace(/\s+/g, ' ');
            return title;
        }

        /** Should this link be tracked? (e.g., not a simple link to a local .html file) */
        get trackable() {
            return !(this.local && /\.html$/.test(this.path));
        }

        /** An object containing structured data for analytics tracking. */
        get trackingData() {
            let host = this.linkHost, path = this.path;
            if (this.proxy || this.proxyLogin) {
                try {
                    const proxiedUrl = new URL(this.url);
                    host = proxiedUrl.hostname;
                    path = proxiedUrl.pathname;
                } catch (e) {
                    // Handle cases where the proxied URL is invalid.
                    host = 'invalid-proxied-url';
                    path = '/';
                }
            }

            const title = this.title;
            const external = !this.local;
            const query = external ? "" : this.#node.search;

            return {
                host,
                path,
                query,
                title,
                external
            };
        }

        /** The "real" URL, with the proxy part removed if present. */
        get url() {
            let href = this.#node.href;
            if (this.proxy || this.proxyLogin) {
                // href = href.substring(href.indexOf("url=") + 4);
                const params = new URL(href).searchParams;
                return params.get('url') || href;
            }
            return href;
        }
    }

    L.LinkInfo = LinkInfo;

})();
