package edu.stanford.irt.laneweb.flickr;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class FlickrPhotoListService {

    private List<FlickrPhoto> photos;

    private Random random = new Random();

    public FlickrPhotoListService(final List<FlickrPhoto> photos) {
        this.photos = new ArrayList<>(photos);
    }

    public List<FlickrPhoto> getRandomPhotos(final int number) {
        int size = this.photos.size();
        Set<Integer> generated = new LinkedHashSet<>();
        if (size > number) {
            while (generated.size() < number) {
                Integer next = this.random.nextInt(size);
                generated.add(next);
            }
        }
        return generated.stream().map(this.photos::get).collect(Collectors.toList());
    }
}
