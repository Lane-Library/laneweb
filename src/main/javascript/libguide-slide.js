$(function() {
	   function left(element, using) {
            element.position({
                my : "right",
                at : "left",
                of : "#libguide-slide",
                collision : "none",
                using : using
            });
        }
        function right(element, using) {
            element.position({
                my : "left",
                at : "right",
                of : "#libguide-slide",
                collision : "none",
                using : using
            });
        }
        function center(element, using) {
            element.position({
                my : "center",
                at : "center",
                of : "#libguide-slide",
                using : using
            });
        }

        function animate(to) {
            $(this).stop(true, false).animate(to);
        }

        center($(".slide:eq(1)"));
        left($(".slide:eq(0)"));
        right($(".slide:eq(2)"));

        function next(event) {
            event.preventDefault();
            center($(".slide:eq(2)"), animate);
            left($(".slide:eq(1)"), animate);
            right($(".slide:eq(0)").appendTo("#libguide-slide"));
        }

        function previous(event) {
            event.preventDefault();
            center($(".slide:eq(0)"), animate);
            right($(".slide:eq(1)"), animate);
            left($(".slide:eq(2)").prependTo("#libguide-slide"));
        }

        $("#previous-libguide-slide").on("click", previous);

        $("#next-libguide-slide").on("click", next);

        /* $(".slide").on("click", function(event) {
            $(".slide").index(this) === 0 ? previous(event) : next(event);
        }); */


        $(window).on("resize", function() {
            left($(".slide:eq(0)"));
            center($(".slide:eq(1)"));
            right($(".slide:eq(2)"));
        });



    });