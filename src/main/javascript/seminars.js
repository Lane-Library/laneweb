(function() {

    "use strict";

    var seminars = document.querySelectorAll('.seminar');
    if (seminars) {
        var now = new Date(),
            currentYear = now.getFullYear(),
            currentMonth = now.getMonth(),
            processSeminars = function(parent) {
                var mySeminars = parent.querySelectorAll('.seminar'),
                    hiddenSeminarArray = [],
                    visibleSeminarCount = 0;
                mySeminars.forEach(function(node) {
                    if (node.style.display === 'none') {
                        hiddenSeminarArray.push(node);
                    } else {
                        visibleSeminarCount++;
                    }
                });

                mySeminars.forEach(function(node) {
                    var month = node.querySelector('.month'),
                        day = node.querySelector('.day'),
                        endTime = node.querySelector('.time'),
                        date, eventMonth;
                    if (month && day && endTime) {
                        endTime = endTime.textContent.toString().replace(/.* - (\d{1,2}:\d{2} .m)$/, "$1");
                        date = month.textContent + " " + day.textContent + ", " + currentYear;
                        eventMonth = new Date(date).getMonth();
                        // add one to currentYear when event month is less than current month (like in December when upcoming event is in January)
                        if (eventMonth < currentMonth) {
                            date = month.textContent + " " + day.textContent + ", " + (currentYear + 1);
                        }
                        if (visibleSeminarCount-- > 0 && Date.parse(date + " " + endTime) < Date.now()) {
                            node.style.display = "none";
                            if (hiddenSeminarArray.length > 0) {
                                hiddenSeminarArray.shift().style.display = "block";
                            }
                        }
                    }
                });
                parent.dataset.processed = true;
            };

        seminars.forEach(function(node) {
            if (node.parentNode.dataset.processed === undefined) {
                processSeminars(node.parentNode);
            }
        });
    }
})();
