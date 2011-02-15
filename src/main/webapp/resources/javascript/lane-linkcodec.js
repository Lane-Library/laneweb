/**
 * LinkCodec is a class that decodes (and maybe later) codes link urls
 */
(function() {

    LinkCodec = function() {
        var thisHost = document.location.host,
            host = "host",
            pathname = "pathname",
            loginPath = "/././secure/apps/proxy/credential",
            proxyHost = "laneproxy.stanford.edu";

        return {

            isLocal : function(link) {
                return link.get(host) === thisHost ? !this.isProxyLogin(link) : false;
            },

            isProxyLogin : function(link) {
                if (link.get(host) !== thisHost) {
                    return false;
                } else {
                	var pathIndex = link.get(pathname).indexOf(loginPath);
                    return pathIndex === 0 || pathIndex === 1;
                }
            },

            isProxy : function(link) {
            	var pathIndex = link.get(pathname).indexOf("login");
                return link.get(host).indexOf(proxyHost) === 0 && (pathIndex === 0 || pathIndex === 1);
            }
        };
    };

    Y.lane.LinkCodec = new LinkCodec();

})();