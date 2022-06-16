(function() {

    "use strict";


    if (document.querySelector(".guide")) {

        var defaultGuide = '#all-guides',
            allGuidesClosed = '#off',

            model = function(id) {
                if (!id)
                    id = defaultGuide
                var m = {
                    target: id,
                }
                return m;
            }(window.location.hash),

            view = function() {
                return {
                    clearMenu: function() {
                        document.querySelectorAll('.menu-guide ul li a.menuitem-active').forEach(function(anchor) {
                            anchor.classList.remove('menuitem-active');

                        })
                    },
                    activeMenu: function(target) {
                        document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
                            if (anchor.href.endsWith(target)) {
                                anchor.classList.add('menuitem-active');
                            }
                        })
                    }
                    ,
                    openGuide: function(target) {
                        if (target != allGuidesClosed) {
                            document.querySelector(target).classList.add('active');
                        }
                    },
                    closesAllGuides: function() {
                        document.querySelectorAll('.libguides div.active').forEach(function(div) {
                            div.classList.remove('active');
                        })
                    }
                }
            }(),

            controller = function() {
                return {
                    load: function() {
                        view.clearMenu();
                        view.activeMenu(model.target);
                        view.openGuide(model.target);
                    },
                    reload: function() {
                        view.clearMenu();
                        view.activeMenu(model.target);
                        view.closesAllGuides();
                        view.openGuide(model.target);
                    },
                    unactiveAll: function() {
                        view.clearMenu();
                        view.closesAllGuides();
                    }
                }
            }();



        window.addEventListener("load", function() {
            controller.load();

            document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
                anchor.addEventListener('click', function(event) {
                    model.target = event.target.href.substring(event.target.href.indexOf('#'));
                    controller.reload();
                });
            });

            document.querySelectorAll('.guide-menu-toggle.on').forEach(function(menubutton) {
                menubutton.addEventListener('click', function(event) {
                    model.target = "#" + event.currentTarget.dataset.target;
                    controller.reload();
                });
            });

            document.querySelectorAll('.guide-menu-toggle.off').forEach(function(menubutton) {
                menubutton.addEventListener('click', function(event) {
                    controller.unactiveAll();

                });
            });
        }, false);
    }
})();
