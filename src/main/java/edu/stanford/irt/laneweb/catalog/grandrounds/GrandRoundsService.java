package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.util.List;

import edu.stanford.irt.grandrounds.Presentation;

public interface GrandRoundsService {

    List<Presentation> getByYear(String department, String year);

    List<Presentation> getRecent(String department, String limit);
}
