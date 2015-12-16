(function() {
    var seminars = Y.all('.seminar');
    if (seminars) {
        var currentYear = new Date().getFullYear(),
        visibleSeminars = seminars.size();
        Y.all('.seminar').each(
            function(n) {
                var month = this.one('.month').get('text'), 
                day = this.one('.day').get('text'), 
                endTime = this.one('.time').get('text'), date;
                if (month && day && endTime) {
                    endTime = endTime.toString().replace(/.* - (\d{1,2}:\d{2} .m)$/, "$1");
                    date = month + " " + day + ", " + currentYear;
                    if (--visibleSeminars > 0 && Date.parse(date + " " + endTime) < Date.now()) {
                        this.setStyles({
                            display : "none"
                        });
                    }
                }
            });
    }
})();