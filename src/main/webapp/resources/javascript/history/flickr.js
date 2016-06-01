(function() {

    var images = [],

        flickrPhotos = document.querySelector(".flickr-photos"),

        links = flickrPhotos.querySelectorAll("a"),

        photoService = {

            getFactor: function(images) {
                var width = 0,
                    factor = 1000,
                    i = 0;

                while (i < 6 && factor > 1) {
                    width += images[i++].width;
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
                request.open("GET", "../apps/getFlickrPhotoList", true);
                request.onload = function() {
                    if (request.status >= 200 && request.status < 400) {
                        var photos = JSON.parse(request.responseText);
                        for(var i = 0; i < 6; i++) {
                            photos[i].image = new Image();
                            photos[i].image.src = photos[i].thumbnail;
                        }
                        setTimeout(function() {
                            flickrPhotos.style.opacity = 0;
                            setTimeout(function() {
                                var factor, i;
                                images = [];
                                for (i = 0; i < 6; i++) {
                                    photoService.resize(photos[i].image);
                                    images.push(photos[i].image);
                                }
                                factor = photoService.getFactor(images);
                                for (i = 0; i < 6; i++) {
                                    links.item(i).style.width = Math.round(photos[i].image.width * factor) + "px";
                                    links.item(i).href = photos[i].page;
                                    links.item(i).replaceChild(photos[i].image, links.item(i).firstChild);
                                }
                                flickrPhotos.style.opacity = 1;
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
            if (links.length != images.length) {
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
            factor = photoService.getFactor(images);
            for (i = 0; i < 6; i++) {
                images[i].parentNode.style.width = Math.round(images[i].width * factor) + "px";
            }
            flickrPhotos.style.opacity = 1;
            photoService.getPhotos();
        };

    [].forEach.call(flickrPhotos.querySelectorAll("img"), function(image) {
        images.push(image);
        if (image.complete) {
            photoService.resize(image);
            if (imagesComplete()) {
                handleImagesComplete();
            }
        } else {
            image.onload = function() {
                var i, factor;
                photoService.resize(this);
                if (imagesComplete()) {
                    handleImagesComplete();
                }
            };
        }
    });
})();