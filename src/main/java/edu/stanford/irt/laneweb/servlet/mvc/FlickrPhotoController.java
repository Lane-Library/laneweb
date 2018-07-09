package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.flickr.FlickrPhoto;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoListService;

@Controller
public class FlickrPhotoController {

    private static final int RANDOM_PHOTO_COUNT = 6;

    private FlickrPhotoListService service;

    public FlickrPhotoController(final FlickrPhotoListService service) {
        this.service = service;
    }

    @GetMapping(value = "/apps/getFlickrPhotoList")
    @ResponseBody
    public List<FlickrPhoto> getFlickrPhotoList(final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return this.service.getRandomPhotos(RANDOM_PHOTO_COUNT);
    }
}
