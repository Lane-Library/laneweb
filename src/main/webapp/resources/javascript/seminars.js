(function() {
    var seminars = Y.all('.seminar');
    if (seminars) {
        var currentYear = new Date().getFullYear(),
        parents = [];
        seminars.each(
                function() {
                    if (parents.indexOf(this.ancestor()) < 0) {
                        parents.push(this.ancestor());
                    }
                }
        );
        for (var i = 0; parents.length > i; i++) {
            var mySeminars = parents[i].all('.seminar'),
            myHiddenSeminars = [];
            mySeminars.each(
                    function() {
                        if (this.getStyle('display') == 'none') {
                            myHiddenSeminars.push(this);
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
                            if (Date.parse(date + " " + endTime) < Date.now()) {
                                this.setStyles({
                                    display : "none"
                                });
                                if (myHiddenSeminars.length > 0) {
                                    myHiddenSeminars.shift().setStyles({display : "block"});
                                }
                            }
                        }
                    }
            );
        }
    }
})();