(function() {
	// case 112758: block non-Stanford sites from framing laneweb content
	// (Qualys clickjacking)
	var selfLocation = self.location;
	if (self !== top && selfLocation.hostname.indexOf('.stanford.edu') === -1) {
		top.location = selfLocation;
	}
})();