/**
 * LinkCodec is a class that decodes (and maybe later) codes link urls
 */
(function() {

    LinkCodec = function() {
        var thisHost = document.location.host,
            host = "host",
            pathname = "pathname",
            loginPath = "/././secure/apps/proxy/credential",
            proxyHost = "laneproxy.stanford.edu",
            
            getPath = function(link) {
                var path = link.get(pathname);
                return path.indexOf("/") === 0 ? path : "/" + path;
            };

        return {

            isLocal : function(link) {
                return link.get(host) === thisHost ? !this.isProxyLogin(link) : false;
            },

            isProxyLogin : function(link) {
                if (link.get(host) !== thisHost) {
                    return false;
                } else {
                    return getPath(link) === loginPath;
                }
            },

            isProxy : function(link) {
                return getPath(link) === "/login";
            }
        };
    };

    Y.lane.LinkCodec = new LinkCodec();

})();