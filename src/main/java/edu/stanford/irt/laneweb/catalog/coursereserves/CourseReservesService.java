package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.List;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;

public interface CourseReservesService {

    List<Course> getCourses();

    CourseReservesItemList getItems();

    CourseReservesItemList getItems(String id);
}
