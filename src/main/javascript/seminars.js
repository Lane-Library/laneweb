(function() {

    "use strict";

    var seminars = Y.all('.seminar');
    if (seminars) {
        var now = new Date(),
        currentYear = now.getFullYear(),
        currentMonth = now.getMonth(),
        processSeminars = function(parent) {
            var mySeminars = parent.all('.seminar'),
            hiddenSeminarArray = [],
            visibleSeminarCount = 0;
            mySeminars.each(
                    function() {
                        if (this.getStyle('display') === 'none') {
                            hiddenSeminarArray.push(this);
                        } else {
                            visibleSeminarCount++;
                        }
                    }
            );
            mySeminars.each(
                    function() {
                        var month = this.one('.month'),
                        day = this.one('.day'),
                        endTime = this.one('.time'),
                        date,
                        eventMonth;
                        if (month && day && endTime) {
                            endTime = endTime.get('text').toString().replace(/.* - (\d{1,2}:\d{2} .m)$/, "$1");
                            date = month.get('text') + " " + day.get('text') + ", " + currentYear;
                            eventMonth = new Date(date).getMonth();
                            // add one to currentYear when event month is less than current month (like in December when upcoming event is in January)
                            if (eventMonth < currentMonth) {
                                date = month.get('text') + " " + day.get('text') + ", " + (currentYear + 1);
                            }
                            if (visibleSeminarCount-- > 0 && Date.parse(date + " " + endTime) < Date.now()) {
                                this.setStyles({
                                    display : "none"
                                });
                                if (hiddenSeminarArray.length > 0) {
                                    hiddenSeminarArray.shift().setStyles({display : "block"});
                                }
                            }
                        }
                    }
            );
            parent.setData('processed',true);
        };
        seminars.each(
                function() {
                    if (this.ancestor().getData('processed') === undefined) {
                        processSeminars(this.ancestor());
                    }
                }
        );
    }
})();
