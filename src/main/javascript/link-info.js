/**
 * LinkInfo is a wrapper for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(function() {

    "use strict";

    let PROXY_HOST_PATTERN = "^(?:login\\.)?laneproxy.stanford.edu$",
        PROXY_LOGIN_PATH = "/login",
        basePath = L.Model.get("base-path") || "",
        documentHostName = location.hostname,
        loginPath = basePath + "/secure/apps/proxy/credential",
        linkInfo = function(node) {
            this.node = node;
        };

    linkInfo.prototype = {

        _isLocalPopup : function(node) {
            let rel = node.rel;
            return rel && rel.indexOf("popup local") === 0;
        },

        _getTitleFromImg : function() {
            let i, title, img = this.node.querySelectorAll('img');
            for (i = 0; i < img.length; i++) {
                if (img.item(i).alt) {
                    title = img.item(i).alt;
                } else if (img.item(i).src) {
                    title = img.item(i).src;
                }
                if (title) {
                    break;
                }
            }
            return title;
        }
    };

    Object.defineProperties(linkInfo.prototype, {
        linkHost : {
            writeable: false,
            get: function() {
                let host = this.node.hostname;
                host = host || documentHostName;
                if (host.indexOf(":") > -1) {
                    host = host.substring(0, host.indexOf(":"));
                }
                return host;
            }
        },
        local : {
            writeable: false,
            get: function() {
                return this.linkHost === documentHostName ? !this.proxyLogin : false;
            }
        },
        path : {
            writeable: false,
            get: function() {
                let path = this.node.pathname;
                path = path === undefined || path === "" ? location.pathname : path;
                return path.indexOf("/") === 0 ? path : "/" + path;
            }
        },
        proxy : {
            writeable: false,
            get: function() {
                return this.linkHost.match(PROXY_HOST_PATTERN) && this.path === PROXY_LOGIN_PATH;
            }
        },
        proxyLogin : {
            writeable: false,
            get: function() {
                if (this.linkHost !== documentHostName) {
                    return false;
                } else {
                    return this.path === loginPath;
                }
            }
        },
        query : {
            writeable: false,
            get: function() {
                if (this.local) {
                    return this.node.search;
                } else {
                    //TODO: implement query for external links.
                    return "";
                }
            }
        },
        title : {
            writeable: false,
            get: function() {
                //if there is a title attribute, use that.
                let node = this.node, title = node.title;
                //next try alt attribute.
                if (!title) {
                    title = node.alt;
                }
                //next look for alt or src attributes in any child img.
                if (!title) {
                    title = this._getTitleFromImg(node);
                }
                //next get the text content before any nested markup
                if (!title) {
                    title = node.textContent;
                }
                if (!title) {
                    title = node.innerText;
                }
                //finally:
                if (!title) {
                    title = 'unknown';
                }
                //if there is rel="popup local" then add "pop-up" to the title
                if (this._isLocalPopup(node)) {
                    title = 'YUI Pop-up [local]: ' + title;
                }
                title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
                return title;

            }
        },
        trackable : {
            writeable: false,
            get: function() {
                return !(this.local && (/\.html$/).test(this.path));
            }
        },
        trackingData : {
            writeable: false,
            get: function() {
                let host, path, query, title, external;
                if ( this.proxy || this.proxyLogin) {
                    host = this.url;
                    host = host.substring(host.indexOf("//") + 2);
                    if (host.indexOf("/") > -1) {
                        path = host.substring(host.indexOf("/"));
                        host = host.substring(0, host.indexOf("/"));
                    }
                } else {
                    host = this.linkHost;
                    path = this.path;
                }
                title = this.title;
                external = !this.local;
                query = external ? "" : location.search;
                return {
                    host: host,
                    path: path,
                    query: query,
                    title: title,
                    external: external
                };
            }
        },
        url : {
            writeable: false,
            get: function() {
                let href = this.node.href;
                if (this.proxy || this.proxyLogin) {
                    href = href.substring(href.indexOf("url=") + 4);
                } 
                return href;
            }
        }
    });

    L.LinkInfo = linkInfo;

})();
