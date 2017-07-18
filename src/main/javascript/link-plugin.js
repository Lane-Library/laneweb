/**
 * LinkPlugin is an plugin for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(function() {

    "use strict";

    Y.namespace("lane");

    var ALT = "alt",
        COOKIES_FETCH = "cookiesFetch",
        LINK_HOST = "linkHost",
        LOCAL = "local",
        HOST_NAME = "hostname",
        HOST_NODE = "host",
        PATH = "path",
        PROXY = "proxy",
        PROXY_HOST_PATTERN = "^(?:login\\.)?laneproxy.stanford.edu$",
        PROXY_LOGIN = "proxyLogin",
        PROXY_LOGIN_PATH = "/login",
        SRC = "src",
        TITLE = "title",
        lane = Y.lane,
        location = lane.Location,
        basePath = lane.Model.get("base-path") || "",
        cookiesFetchPath = basePath + "/cookiesFetch.html",
        documentHostName = location.get("hostname"),
        loginPath = basePath + "/secure/apps/proxy/credential",

    LinkPlugin = function(config) {
        LinkPlugin.superclass.constructor.apply(this, arguments);
    };

    LinkPlugin.NS = "link";

    LinkPlugin.NAME = "linkPlugin";

    LinkPlugin.ATTRS = {
            cookiesFetch : {
                readOnly : true,
                valueFn : function() {
                    var path = this.get(PATH);
                    return path !== undefined && path.indexOf(cookiesFetchPath) === 0;
                }
            },
            linkHost : {
                readOnly : true,
                valueFn : function() {
                    var host = this.get(HOST_NODE).get(HOST_NAME);
                    host = host || documentHostName;
                    if (host.indexOf(":") > -1) {
                        host = host.substring(0, host.indexOf(":"));
                    }
                    return host;
                }
            },
            local : {
                readOnly : true,
                valueFn : function() {
                    return this.get(LINK_HOST) === documentHostName ? !this.get(PROXY_LOGIN) && !this.get(COOKIES_FETCH) : false;
                }
            },
            path : {
                readOnly : true,
                valueFn : function() {
                    var path = this.get(HOST_NODE).get("pathname");
                    path = path === undefined || path === "" ? location.get("pathname") : path;
                    return path.indexOf("/") === 0 ? path : "/" + path;
                }
            },
            proxy : {
                readOnly : true,
                valueFn : function() {
                    return this.get(LINK_HOST).match(PROXY_HOST_PATTERN) && this.get(PATH) === PROXY_LOGIN_PATH;
                }
            },
            proxyLogin : {
                readOnly : true,
                valueFn : function() {
                    if (this.get(LINK_HOST) !== documentHostName) {
                        return false;
                    } else {
                        return this.get(PATH) === loginPath;
                    }
                }
            },
            query : {
                readOnly : true,
                valueFn : function() {
                    if (this.get(LOCAL)) {
                        return this.get(HOST_NODE).get("search");
                    } else {
                        //TODO: implement query for external links.
                        return "";
                    }
                }
            },
            title : {
                readOnly : true,
                valueFn : function() {
                    //if there is a title attribute, use that.
                    var node = this.get(HOST_NODE), title = node.get(TITLE);
                    //next try alt attribute.
                    if (!title) {
                        title = node.get(ALT);
                    }
                    //next look for alt or src attributes in any child img.
                    if (!title) {
                        title = this._getTitleFromImg(node);
                    }
                    //next get the text content before any nested markup
                    if (!title) {
                        title = node.get('text');
                    }
                    if (!title) {
                        title = node.get('innerText');
                    }
                    //finally:
                    if (!title) {
                        title = 'unknown';
                    }
                    if (node.ancestor(".lane-nav")) {
                        title = "laneNav: " + title;
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
                readOnly : true,
                valueFn : function() {
                    return !(this.get(LOCAL) && (/\.html$/).test(this.get(PATH)));
                }
            },
            trackingData : {
                readOnly : true,
                valueFn : function() {
                    var host, path, query, title, external;
                    if (this.get(COOKIES_FETCH) || this.get(PROXY) || this.get(PROXY_LOGIN)) {
                        host = this.get("url");
                        host = host.substring(host.indexOf("//") + 2);
                        if (host.indexOf("/") > -1) {
                            path = host.substring(host.indexOf("/"));
                            host = host.substring(0, host.indexOf("/"));
                        }
                    } else {
                        host = this.get(LINK_HOST);
                        path = this.get(PATH);
                    }
                    title = this.get(TITLE);
                    external = !this.get(LOCAL);
                    query = external ? "" : location.get("search");
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
                readOnly : true,
                valueFn : function() {
                    var href = this.get(HOST_NODE).get("href");
                    if (this.get(PROXY) || this.get(PROXY_LOGIN)) {
                        href = href.substring(href.indexOf("url=") + 4);
                    } else if (this.get(COOKIES_FETCH)) {
                        href = href.substring(href.indexOf("url=") + 4);
                        href = href.substring(0, href.indexOf("&"));
                        href = window.decodeURIComponent(href);
                    }
                    return href;
                }
            }
    };

    Y.extend(LinkPlugin, Y.Plugin.Base, {

        _isLocalPopup : function(node) {
            var rel = node.getAttribute("rel");
            return rel && rel.indexOf("popup local") === 0;
        },

        _getTitleFromImg : function(node) {
            var i, title, img = node.all('img');
            if (img) {
                for (i = 0; i < img.size(); i++) {
                    if (img.item(i).get(ALT)) {
                        title = img.item(i).get(ALT);
                    } else if (img.item(i).get(SRC)) {
                        title = img.item(i).get(SRC);
                    }
                    if (title) {
                        break;
                    }
                }
            }
            return title;
        }
    });

    lane.LinkPlugin = LinkPlugin;

})();