(function() {
    var seminars = Y.all('.seminar');
    if (seminars) {
        var currentYear = new Date().getFullYear(),
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
                        var month = this.one('.month').get('text'),
                        day = this.one('.day').get('text'),
                        endTime = this.one('.time').get('text'), date;
                        if (month && day && endTime) {
                            endTime = endTime.toString().replace(/.* - (\d{1,2}:\d{2} .m)$/, "$1");
                            date = month + " " + day + ", " + currentYear;
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