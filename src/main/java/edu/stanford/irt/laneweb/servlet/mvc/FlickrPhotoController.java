package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.flickr.FlickrPhoto;
import edu.stanford.irt.laneweb.flickr.FlickrPhotoListService;

@Controller
public class FlickrPhotoController {

    private static final int RANDOM_PHOTO_COUNT = 6;

    private FlickrPhotoListService service;

    @Autowired
    public FlickrPhotoController(final FlickrPhotoListService service) {
        this.service = service;
    }

    @RequestMapping("/apps/getFlickrPhotoList")
    @ResponseBody
    public List<FlickrPhoto> getFlickrPhotoList(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return this.service.getRandomPhotos(RANDOM_PHOTO_COUNT);
    }
}
