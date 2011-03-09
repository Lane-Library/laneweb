/**
 * LinkPlugin is an plugin for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(function() {
    
    var LinkPlugin = function(config) {
        LinkPlugin.superclass.constructor.apply(this, arguments);
    };
    
    LinkPlugin.NS = "link";
    
    LinkPlugin.NAME = "linkPlugin";
    
    LinkPlugin.COOKIES_FETCH_PATH = "/././cookiesFetch.html";
    
    LinkPlugin.DOCUMENT_HOST = document.location.host;
    
    LinkPlugin.LOGIN_PATH = "/././secure/apps/proxy/credential";
    
    LinkPlugin.PROXY_HOST = "laneproxy.stanford.edu";
    
    LinkPlugin.PROXY_LOGIN_PATH = "/login";
    
    LinkPlugin.ATTRS = {
            cookiesFetch : {
                readOnly : true,
                valueFn : function() {
                    var path = this.get("path");
                    return path !== undefined && path.indexOf(LinkPlugin.COOKIES_FETCH_PATH) === 0;
                }
            },
            linkHost : {
                readOnly : true,
                valueFn : function() {
                    var host = this.get("host").get("host");
                    return host !== undefined ? host : LinkPlugin.DOCUMENT_HOST;
                }
            },
            local : {
                readOnly : true,
                valueFn : function() {
                    return this.get("linkHost") === LinkPlugin.DOCUMENT_HOST ?
                            !this.get("proxyLogin") && !this.get("cookiesFetch")
                            : false;
                }
            },
            path : {
                readOnly : true,
                valueFn : function() {
                    var path = this.get("host").get("pathname");
                    path = path === undefined ? document.location.pathname : path;
                    return path.indexOf("/") === 0 ? path : "/" + path;
                }
            },
            proxy : {
                readOnly : true,
                valueFn : function() {
                    return this.get("linkHost") === LinkPlugin.PROXY_HOST
                    && this.get("path") === LinkPlugin.PROXY_LOGIN_PATH;
                }
            },
            proxyLogin : {
                readOnly : true,
                valueFn : function() {
                    if (this.get("linkHost") !== LinkPlugin.DOCUMENT_HOST) {
                        return false;
                    } else {
                        return this.get("path") === LinkPlugin.LOGIN_PATH;
                    }
                }
            },
            title : {
                readOnly : true,
                valueFn : function() {
                    //if there is a title attribute, use that.
                    var node = this.get("host"), title = node.get('title'), img, i, rel, relTokens;
                    //if there is rel="popup .." then create a title from it.
                    rel = node.get('rel');
                    if (rel && rel.indexOf('popup') === 0) {
                        relTokens = rel.split(' ');
                        if (relTokens[1] == 'local' || relTokens[1] == 'faq') {
                            title = 'YUI Pop-up [' + relTokens[1] + ']';
                        }
                    }
                    //next try alt attribute.
                    if (!title) {
                        title = node.get('alt');
                    }
                    //next look for alt attributes in any child img.
                    if (!title) {
                        img = node.all('img');
                        if (img) {
                            for (i = 0; i < img.size(); i++) {
                                if (img.item(i).get('alt')) {
                                    title = img.item(i).get('alt');
                                    break;
                                }
                                else if (img.item(i).get('src')) {
                                    title = img.item(i).get('src');
                                    break;
                                }
                            }
                        }
                    }
                    //next get the text content before any nested markup
                    if (!title) {
                        title = node.get('textContent');
                    }
                    if (!title) {
                        title = node.get('innerText');
                    }
                    if (title) {
                        //trim and normalize:
                        title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
                    }
                    //finally:
                    if (!title) {
                        title = 'unknown';
                    }
                    if (node.hasClass('yui3-accordion-item-trigger')) {
                        title = 'Expandy:' + title;
                    } else if (node.ancestor("#laneNav")) {
                        title = "laneNav: " + title;
                    }
                    return title;
                
                }
            },
            trackable : {
                readOnly : true,
                valueFn : function() {
                    if (this.get("host").getAttribute("trackable")) {
                        return true;
                    } else if (this.get("local") && (/\.html$/).test(this.get("path"))) {
                        return false;
                    } else {
                        return true;
                    }
                }
            },
            trackingData : {
                readOnly : true,
                valueFn : function() {
                    var host, path, query, title, external;
                    if (this.get("cookiesFetch") || this.get("proxy") || this.get("proxyLogin")) {
                        host = this.get("url");
                        host = host.substring(host.indexOf("//") + 2);
                        if (host.indexOf("/") > -1) {
                            path = host.substring(host.indexOf("/"));
                            host = host.substring(0, host.indexOf("/"));
                        }
                    } else {
                        host = this.get("linkHost");
                        path = this.get("path");
                    }
                    title = this.get("title");
                    external = !this.get("local");
                    query = external ? "" : document.location.search;
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
                    var href = this.get("host").get("href");
                    if (this.get("proxy") || this.get("proxyLogin")) {
                        href = href.substring(href.indexOf("url=") + 4);
                    } else if (this.get("cookiesFetch")) {
                        href = href.substring(href.indexOf("url=") + 4);
                        href = href.substring(0, href.indexOf("&"));
                        href = window.decodeURIComponent(href);
                    }
                    return href;
                }
            }
    };

    Y.extend(LinkPlugin, Y.Plugin.Base);
    
    Y.lane.LinkPlugin = LinkPlugin;

})();