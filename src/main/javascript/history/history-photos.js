(() => {

    "use strict";

    const historyPhotosContainer = document.querySelector(".history-photos");

    if (!historyPhotosContainer) {
        return;
    }

    let images = [];
    const links = historyPhotosContainer.querySelectorAll("a");
    const TOTAL_PHOTOS = 6;
    const ROTATION_INTERVAL_MS = 15000;
    const FADE_DURATION_MS = 1000;
    const TARGET_ROW_WIDTH = 989;
    const TARGET_IMAGE_HEIGHT = 200;

    const getFactor = (imagesToFactor) => {
        let width = 0;
        const relevantImages = imagesToFactor.slice(0, TOTAL_PHOTOS);
        if (relevantImages.length === 0) return 1;

        for (const image of relevantImages) {
            width += image.width;
        }
        return TARGET_ROW_WIDTH / width;
    }

    const resizeImage = (image) => {
        if (image.height > 0) {
            const aspect = image.width / image.height;
            image.height = TARGET_IMAGE_HEIGHT;
            image.width = TARGET_IMAGE_HEIGHT * aspect;
        }
    }

    const fetchPhotos = () => {
        fetch("../apps/getHistoryPhotoList")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(photos => {
                photos.slice(0, TOTAL_PHOTOS).forEach(photo => {
                    photo.image = new Image();
                    photo.image.src = photo.thumbnail;
                    photo.image.alt = photo.title;
                });
                setTimeout(() => {
                    historyPhotosContainer.style.opacity = 0;
                    setTimeout(() => {
                        let factor;
                        images = [];
                        images = photos.slice(0, TOTAL_PHOTOS).map(photo => {
                            resizeImage(photo.image);
                            return photo.image;
                        });
                        factor = getFactor(images);
                        photos.slice(0, TOTAL_PHOTOS).forEach((photo, j) => {
                            const link = links[j];
                            if (link) {
                                link.style.width = `${Math.round(photo.image.width * factor)}px`;
                                link.href = photo.page;
                                link.replaceChild(photo.image, link.firstChild);
                            }
                        });
                        historyPhotosContainer.style.opacity = 1;
                    }, FADE_DURATION_MS);
                    fetchPhotos();
                }, ROTATION_INTERVAL_MS);
            })
            .catch(error => {
                console.error("Failed to fetch photo list:", error);
            });
    }

    const imagesComplete = () => {
        if (links.length !== images.length) {
            return false;
        }
        return images.every(image => image.complete);
    };

    const handleImagesComplete = () => {
        const factor = getFactor(images);
        images.slice(0, TOTAL_PHOTOS).forEach(image => {
            image.parentNode.style.width = `${Math.round(image.width * factor)}px`;
        });
        historyPhotosContainer.style.opacity = 1;
        fetchPhotos();
    };

    const initialImages = historyPhotosContainer.querySelectorAll("img");
    initialImages.forEach((image) => {
        images.push(image);
        const processImage = () => {
            resizeImage(image);
            if (imagesComplete()) {
                handleImagesComplete();
            }
        };

        if (image.complete) {
            processImage();
        } else {
            image.onload = processImage;
            image.onerror = () => {
                console.error("Initial image failed to load", image.src);
                // Still process to not hang the script
                processImage();
            };
        }
    });

})();
