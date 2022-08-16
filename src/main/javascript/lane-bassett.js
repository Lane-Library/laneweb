(function() {

    "use strict";

    var bassettContent = document.querySelector('#bassettContent'),
        model = L.Model,
        basePath = model.get(model.BASE_PATH)|| "",
        diagramDisplay = false,
        accordion,
        history = window.history,
        subRegionToShow = 4,
        prevRegion,
        prevSubRegion,

        formatAjaxUrl = function(string) {
            var url, href;
            href = string.replace("search.html", "/biomed-resources/bassett/bassettsView.html");
            href = href.substr(href.indexOf("/bassett/") + 8);
            href = href.split("?");
            if (href.length === 1) {
                url = href[0];
            }
            if (href.length > 1) {
                url = href[0] + "?" + href[1];
            }
            if (diagramDisplay) {
                url = url + "&t=diagram";
            }
            return url;
        },

        submitPagination = function(e) {
            var page = e.target.page.value,
                pages = e.target.pages;
            if (page.match('[^0-9]') || page < 1 || Number(page) > Number(pages.value)){
                e.preventDefault();
                document.querySelectorAll(".bassett-error").forEach(function(node){
                    node.style.display = "block";
                });
                return;
            }
            pages.remove();
        },

        loadContent = function(string) {
            var url = basePath + "/plain/biomed-resources/bassett/raw".concat(string);
            function successHandler(id, o) {
                bassettContent.innerHTML = o.responseText;
                registerLinksContainer(bassettContent);
                document.querySelectorAll('.s-pagination form[name=bassett-pagination]').forEach(function(node) {
                    node.addEventListener('submit', submitPagination);
                });
            }
            L.io(url, {
                on : {
                    success : successHandler
                }
            });
        },

        handleClick = function(ev) {
            var url;
            if (this.id === "diagram-choice") {
                diagramDisplay = true;
            }
            if (this.id === "photo-choice") {
                diagramDisplay = false;
            }
            url = formatAjaxUrl(this.href);
            try {
                history.pushState({bassett: url}, "", "");
                loadContent(url);
            } catch (e) {
                loadContent(url);
            }
            ev.preventDefault();
        },

        initializeHistory = function() {
            if (history.state && history.state.bassett) {
                loadContent(history.state.bassett);
            }
            window.addEventListener("popstate", function(event) {
                if (event.state) {
                    loadContent(event.state && event.state.bassett);
                } else {
                    loadContent(formatAjaxUrl(document.location.href));
                }
            });
        },

        registerLinksContainer = function(container) {
            if (container) {
                var anchor = container.querySelectorAll('a');
                for (var i = 0; i < anchor.length; i++) {
                    if (!anchor[i].rel || anchor[i].rel === "propagation") {
                        anchor[i].addEventListener('click', handleClick);
                    }
                }
            }
        },

        // For the bassett menu
        hideSubRegions = function(region) {
            if (prevRegion && prevRegion.querySelector('.see-all')) {
                prevRegion.querySelector('.see-all').innerHTML = 'see all';
                var subRegion = prevRegion.querySelectorAll('li');
                for (var i = subRegionToShow; i < subRegion.length; i++) {
                    subRegion[i].hidden = "hidden";
                    subRegion[i].style.display = "none";
                }
            }
            prevRegion = region;
        },

        resetSubRegion = function(subRegion) {
            if (prevSubRegion && prevSubRegion.querySelector('i')) {
                prevSubRegion.classList.remove('enabled');
                var iElement = prevSubRegion.querySelector('i');
                iElement.classList.add('fa-square');
                iElement.classList.remove('fa-check-square');
            }
            prevSubRegion = subRegion;
        },

        expandSubRegion = function(event) {
            var i, subRegion, display,
                region = event.currentTarget.closest("ul");
            display = region.querySelectorAll('li')[subRegionToShow + 1].style.display;
            hideSubRegions(region);
            resetSubRegion();
            if (display === 'none') {
                region.querySelector('.see-all').innerHTML = 'hide';
                subRegion = region.querySelectorAll('li');
                for (i = subRegionToShow + 1; i < subRegion.length; i++) {
                    subRegion[i].style.display = 'block';
                }
            }
        },

        surlineSubRegion = function(event) {
            var i,  li = event.currentTarget;
            resetSubRegion(li);
            li.classList.add('enabled');
            i = li.querySelector('i');
            i.classList.remove('fa-square');
            i.classList.add('fa-check-square');
        };

    if (bassettContent) {
        accordion = document.querySelector('#bassett-menu');
        // not if largerView.html
        if (accordion) {
            registerLinksContainer(accordion);
            registerLinksContainer(bassettContent);
            document.querySelectorAll('.see-all').forEach(function(node) {
                node.addEventListener('click', expandSubRegion);
            });
            document.querySelectorAll('.region li:not(:first-child)').forEach(function(node) {
                node.addEventListener('click', surlineSubRegion);
            });
            document.querySelectorAll('.s-pagination form[name=bassett-pagination]').forEach(function(node) {
                node.addEventListener('submit', submitPagination);
            });
            initializeHistory();
        }
    }

})();
