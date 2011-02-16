/**
 * Link is an object to be mixed with anchor nodes to provide
 * valuable information about such things as proxy status, etc.
 */
(function() {

	Y.lane.Link = {
			
		thisHost : document.location.host,
		
		host : "host",
		
		pathname : "pathname",
		
		loginPath : "/././secure/apps/proxy/credential",
		
		proxyHost : "laneproxy.stanford.edu",

		getPath : function() {
			var path = this.get(this.pathname);
			return path.indexOf("/") === 0 ? path : "/" + path;
		},
		
		isLocal : function() {
			return this.get(this.host) === this.thisHost ?
					!this.isProxyLogin()
					: false;
		},

		isProxyLogin : function() {
			if (this.get(this.host) !== this.thisHost) {
				return false;
			} else {
				return this.getPath() === this.loginPath;
			}
		},

		isProxy : function() {
			return this.get(this.host) == this.proxyHost
			       && this.getPath() === "/login";
		}
	};

})();