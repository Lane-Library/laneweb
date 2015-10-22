(function() {

    Y.all(".golfclub").each(function() {
        this.set("innerHTML", "<span><span>" + this.get("innerHTML") + "</span></span>");
    });

})();