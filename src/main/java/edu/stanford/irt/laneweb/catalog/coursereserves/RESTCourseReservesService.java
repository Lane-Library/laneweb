package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.net.URI;
import java.util.List;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTCourseReservesService implements CourseReservesService {

    private static final String COURSES_ENDPOINT_PATH = "coursereserves/courses";

    private static final String ITEMS_ENDPOINT_PATH = "coursereserves/items";

    private static final String ITEMS_BY_ID_ENDPOINT_PATH_FORMAT = ITEMS_ENDPOINT_PATH + "?id=%s";

    private static final TypeReference<List<Course>> TYPE = new TypeReference<List<Course>>() {
    };

    public RESTService restService;

    private URI catalogServiceURI;

    public RESTCourseReservesService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Course> getCourses() {
        URI uri = this.catalogServiceURI.resolve(COURSES_ENDPOINT_PATH);
        return this.restService.getObject(uri, TYPE);
    }

    @Override
    public CourseReservesItemList getItems() {
        URI uri = this.catalogServiceURI.resolve(ITEMS_ENDPOINT_PATH);
        return this.restService.getObject(uri, CourseReservesItemList.class);
    }

    @Override
    public CourseReservesItemList getItems(final int id) {
        String pathWithIDParam = String.format(ITEMS_BY_ID_ENDPOINT_PATH_FORMAT, id);
        URI uri = this.catalogServiceURI.resolve(pathWithIDParam);
        return this.restService.getObject(uri, CourseReservesItemList.class);
    }
}
