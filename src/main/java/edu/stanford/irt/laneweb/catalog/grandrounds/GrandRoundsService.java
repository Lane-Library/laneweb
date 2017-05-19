package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.util.List;

import edu.stanford.irt.grandrounds.Presentation;

public interface GrandRoundsService {

    List<Presentation> getGrandRounds(String department, String year);
}