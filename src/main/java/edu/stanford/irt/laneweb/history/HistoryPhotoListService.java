package edu.stanford.irt.laneweb.history;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HistoryPhotoListService {

    private List<HistoryPhoto> photos;

    private Random random = new Random();

    public HistoryPhotoListService(final List<HistoryPhoto> photos) {
        this.photos = new ArrayList<>(photos);
    }

    public List<HistoryPhoto> getRandomPhotos(final int number) {
        int size = this.photos.size();
        Set<Integer> generated = new LinkedHashSet<>();
        if (size > number) {
            while (generated.size() < number) {
                Integer next = this.random.nextInt(size);
                generated.add(next);
            }
        }
        return generated.stream().map(this.photos::get).toList();
    }
}
