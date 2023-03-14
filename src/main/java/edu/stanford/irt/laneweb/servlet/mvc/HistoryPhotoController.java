package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.history.HistoryPhotoListService;
import edu.stanford.irt.laneweb.history.HistoryPhoto;

@Controller
public class HistoryPhotoController {

    private static final int RANDOM_PHOTO_COUNT = 6;

    private HistoryPhotoListService service;

    public HistoryPhotoController(final HistoryPhotoListService service) {
        this.service = service;
    }

    @GetMapping(value = "/apps/getHistoryPhotoList")
    @ResponseBody
    public List<HistoryPhoto> getHistoryPhotoList(final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return this.service.getRandomPhotos(RANDOM_PHOTO_COUNT);
    }
}
