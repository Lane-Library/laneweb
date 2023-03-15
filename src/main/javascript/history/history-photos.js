(function() {

    "use strict";

    var images = [],

        historyPhotos = document.querySelector(".history-photos"),

        links = historyPhotos ? historyPhotos.querySelectorAll("a") : {},

        photoService = {

            getFactor: function(imagesToFactor) {
                var width = 0,
                    factor = 1000,
                    i = 0;

                while (i < 6 && factor > 1) {
                    width += imagesToFactor[i++].width;
                    factor = 989 / width;
                }

                return factor;
            },

            resize: function(image) {
                var aspect = image.width / image.height;
                image.height = 200;
                image.width = 200 * aspect;
            },

            getPhotos: function() {
                var request = new XMLHttpRequest();
                request.open("GET", "../apps/getHistoryPhotoList", true);
                request.onload = function() {
                    if (request.status >= 200 && request.status < 400) {
                        var photos = JSON.parse(request.responseText);
                        for(var i = 0; i < 6; i++) {
                            photos[i].image = new Image();
                            photos[i].image.src = photos[i].thumbnail;
                            photos[i].image.alt = photos[i].title;
                        }
                        setTimeout(function() {
                            historyPhotos.style.opacity = 0;
                            setTimeout(function() {
                                var factor, j;
                                images = [];
                                for (j = 0; j < 6; j++) {
                                    photoService.resize(photos[j].image);
                                    images.push(photos[j].image);
                                }
                                factor = photoService.getFactor(images);
                                for (j = 0; j < 6; j++) {
                                    links.item(j).style.width = Math.round(photos[j].image.width * factor) + "px";
                                    links.item(j).href = photos[j].page;
                                    links.item(j).replaceChild(photos[j].image, links.item(j).firstChild);
                                }
                                historyPhotos.style.opacity = 1;
                            }, 1000);
                            photoService.getPhotos();
                        }, 15000);
                    }
                };
                request.send();
            }
        },

        imagesComplete = function() {
            var complete = true;
            if (links.length !== images.length) {
                complete = false;
            } else {
                images.forEach(function(image) {
                    if (!image.complete) {
                        complete = false;
                    }
                });
            }
            return complete;
        },

        handleImagesComplete = function() {
            var i, factor = photoService.getFactor(images);
            for (i = 0; i < 6; i++) {
                images[i].parentNode.style.width = Math.round(images[i].width * factor) + "px";
            }
            historyPhotos.style.opacity = 1;
            photoService.getPhotos();
        };

    [].forEach.call(historyPhotos ? historyPhotos.querySelectorAll("img") : [], function(image) {
        images.push(image);
        if (image.complete) {
            photoService.resize(image);
            if (imagesComplete()) {
                handleImagesComplete();
            }
        } else {
            image.onload = function() {
                photoService.resize(this);
                if (imagesComplete()) {
                    handleImagesComplete();
                }
            };
        }
    });
})();
