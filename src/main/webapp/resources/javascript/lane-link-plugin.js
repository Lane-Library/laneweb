/**
 * Link is an plugin for anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(function() {
	
	LinkPlugin = function(config) {
		this._node = config.host;
	};
	
    LinkPlugin.NS = "link";
	
    LinkPlugin.DOCUMENT_HOST = document.location.host;
	
    LinkPlugin.HOST = "host";
	
    LinkPlugin.LOGIN_PATH = "/././secure/apps/proxy/credential";
	
    LinkPlugin.PATHNAME = "pathname";
	
    LinkPlugin.PROXY_HOST = "laneproxy.stanford.edu";
    
    LinkPlugin.PROXY_LOGIN_PATH = "/login";

	LinkPlugin.prototype = {

		getPath : function() {
			var path = this._node.get(LinkPlugin.PATHNAME);
			return path.indexOf("/") === 0 ? path : "/" + path;
		},
		
		isLocal : function() {
			return this._node.get(LinkPlugin.HOST) === LinkPlugin.DOCUMENT_HOST ?
					!this.isProxyLogin()
					: false;
		},

		isProxyLogin : function() {
			if (this._node.get(LinkPlugin.HOST) !== LinkPlugin.DOCUMENT_HOST) {
				return false;
			} else {
				return this.getPath() === LinkPlugin.LOGIN_PATH;
			}
		},

		isProxy : function() {
			return this._node.get(LinkPlugin.HOST) == LinkPlugin.PROXY_HOST
			       && this.getPath() === LinkPlugin.PROXY_LOGIN_PATH;
		}
	};
	
	Y.lane.LinkPlugin = LinkPlugin;

})();