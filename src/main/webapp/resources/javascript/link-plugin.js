/**
 * LinkPlugin is an plugin for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
//YUI.add("lane-link-plugin", function(Y) {
(function() {
	
	Y.namespace("lane");
	
	var Lane = Y.lane,
	    basePath = Lane.Model.get("base-path") || "",
	    DOCUMENT_HOST = document.location.host,
	    PROXY_HOST = "laneproxy.stanford.edu",
	    PROXY_LOGIN_PATH = "/login",
	    COOKIES_FETCH_PATH = basePath + "/cookiesFetch.html",
	    LOGIN_PATH = basePath + "/secure/apps/proxy/credential",
	    PATH = "path",
	    HOST = "host",
	    LINK_HOST = "linkHost",
	    PROXY_LOGIN = "proxyLogin",
	    COOKIES_FETCH = "cookiesFetch",
	    LOCAL = "local",
	    TITLE = "title",
	    PROXY = "proxy",
	    ALT = "alt",
	    SRC = "src",
    
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
                    return path !== undefined && path.indexOf(COOKIES_FETCH_PATH) === 0;
                }
            },
            linkHost : {
                readOnly : true,
                valueFn : function() {
                    var host = this.get(HOST).get(HOST);
                    host = host || DOCUMENT_HOST;
                    //TODO: need to check for :443, too?
                    if (host.indexOf(":80") > -1) {
                        host = host.substring(0, host.indexOf(":"));
                    }
                    return host;
                }
            },
            local : {
                readOnly : true,
                valueFn : function() {
                    return this.get(LINK_HOST) === DOCUMENT_HOST ?
                            !this.get(PROXY_LOGIN) && !this.get(COOKIES_FETCH)
                            : false;
                }
            },
            path : {
                readOnly : true,
                valueFn : function() {
                    var path = this.get(HOST).get("pathname");
                    path = path === undefined || path === "" ? document.location.pathname : path;
                    return path.indexOf("/") === 0 ? path : "/" + path;
                }
            },
            proxy : {
                readOnly : true,
                valueFn : function() {
                    return this.get(LINK_HOST) === PROXY_HOST
                    && this.get(PATH) === PROXY_LOGIN_PATH;
                }
            },
            proxyLogin : {
                readOnly : true,
                valueFn : function() {
                    if (this.get(LINK_HOST) != DOCUMENT_HOST) {
                        return false;
                    } else {
                        return this.get(PATH) === LOGIN_PATH;
                    }
                }
            },
            query : {
                readOnly : true,
                valueFn : function() {
                    if (this.get(LOCAL)) {
                        return this.get(HOST).get("search");
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
                    var node = this.get(HOST), title = node.get(TITLE), img, i, rel, relTokens;
                    //if there is rel="popup .." then create a title from it.
                    rel = node.get('rel');
                    if (rel && rel.indexOf('popup') === 0) {
                        relTokens = rel.split(' ');
                        if (relTokens[1] == LOCAL) {
                            title = 'YUI Pop-up [local]';
                        }
                    }
                    //next try alt attribute.
                    if (!title) {
                        title = node.get(ALT);
                    }
                    //next look for alt attributes in any child img.
                    if (!title) {
                        img = node.all('img');
                        if (img) {
                            for (i = 0; i < img.size(); i++) {
                                if (img.item(i).get(ALT)) {
                                    title = img.item(i).get(ALT);
                                    break;
                                }
                                else if (img.item(i).get(SRC)) {
                                    title = img.item(i).get(SRC);
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
                    if (this.get(HOST).getAttribute("trackable")) {
                        return true;
                    } else if (this.get(LOCAL) && (/\.html$/).test(this.get(PATH))) {
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
                    var href = this.get(HOST).get("href");
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

    Y.extend(LinkPlugin, Y.Plugin.Base);
    
    Lane.LinkPlugin = LinkPlugin;
    
})();

//}, "", {
//	requires : ["lane-model", "plugin"]
//});