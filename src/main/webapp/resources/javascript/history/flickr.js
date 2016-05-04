
        (function() {
            
            var photoService = {
                    
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
                        request = new XMLHttpRequest();
                        request.open("GET", "../apps/getFlickrPhotoList", true);
                        request.onload = function() {
                            if (request.status >= 200 && request.status < 400) {
                                var photos = JSON.parse(request.responseText);
                                for(var i = 0; i < 6; i++) {
                                    photos[i].image = new Image();
                                    photos[i].image.src = photos[i].thumbnail;
                                }
                                setTimeout(function() {
                                    var flickr = document.querySelector(".flickr-photos");
                                    var links = flickr.querySelectorAll(".flickr-photos a");
                                    flickr.style.opacity = 0;
                                    setTimeout(function() {
                                        flickr.style.opacity = 1;
                                        images = [];
                                        for (var i = 0; i < 6; i++) {
                                            photoService.resize(photos[i].image);
                                            images.push(photos[i].image);
                                        }
                                        var factor = photoService.getFactor(images);
                                        var width = 0;
                                        for (var i = 0; i < 6; i++) {
                                            links.item(i).style.width = Math.round(photos[i].image.width * factor) + "px";
                                            links.item(i).href = photos[i].page;
                                            links.item(i).replaceChild(photos[i].image, links.item(i).firstChild);
                                        }
                                    }, 1000);
                                    photoService.getPhotos();
                                }, 15000);
                            }
                        };
                        request.send();
                }

            };
            
            var images = [];
            
            var flickr = document.querySelector(".flickr-photos");
            
            
            [].forEach.call(document.querySelectorAll(".flickr-photos img"), function(image) {
                image.onload = function() {
                    photoService.resize(this);
                    images.push(this);
                    if (images.length == 6) {
                        var factor = photoService.getFactor(images);
                        var width = 0;
                        for (var i = 0; i < 6; i++) {
                            images[i].parentNode.style.width = Math.round(images[i].width * factor) + "px";
                        }
                        flickr.style.opacity = 1;


                      photoService.getPhotos();
                    }
                }
            });

        })();