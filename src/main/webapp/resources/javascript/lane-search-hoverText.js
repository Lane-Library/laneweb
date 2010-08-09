// hover text for search result abstracts and descriptions
(function() {
    var Y = LANE.Y, registerHoverTriggers = function() {
        var hoverTargets = Y.all('.hvrTarg'), parentUl, label, i;
        for (i = 0; i < hoverTargets.size(); i++) {
            parentUl = hoverTargets.item(i).ancestor('ul');
            parentUl.addClass('hvrTrig');
            parentUl.on("mouseenter", function(e) {
                this.setStyle("backgroundColor", "#F2F3ED");
                this.setStyle("padding", "4px");
                this.setStyle("border", "1px solid");
                e.currentTarget.one('.hvrTarg').setStyle("display", "block");
                if (this.one(".showAbstract")) {
                    this.one(".showAbstract").setStyle("display", "none");
                }
            });
            parentUl.on("mouseleave", function(e) {
                this.setStyle("backgroundColor", "#fff");
                this.setStyle("padding", "5px");
                this.setStyle("border", "none");
                e.currentTarget.one('.hvrTarg').setStyle("display", "none");
                if (this.one(".showAbstract")) {
                    this.one(".showAbstract").setStyle("display", "block");
                }
            });
            if (navigator.platform.match(/(iPhone|iP.d)/)
                    && !parentUl.one(".showAbstract")) {
                label = (parentUl.one(".pmid")) ? 'Abstract' : 'Description';
                hoverTargets.item(i).insert("<li class='showAbstract'>[<a href='#'>Show " + label + "</a>]</li>", "before");
            }
        }
    };
    Y.Global.on('lane:change', function() {
        registerHoverTriggers();
    });
    registerHoverTriggers();

})();
