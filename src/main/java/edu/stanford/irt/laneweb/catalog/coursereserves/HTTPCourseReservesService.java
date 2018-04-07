package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.CourseReservesService;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.ServiceURIResolver;

public class HTTPCourseReservesService implements CourseReservesService {

    private static final String COURSES_ENDPOINT_PATH = "coursereserves/courses";

    private static final String ITEMS_ENDPOINT_PATH = "coursereserves/items";

    private static final String ITEMS_BY_ID_ENDPOINT_PATH_FORMAT = ITEMS_ENDPOINT_PATH + "?id=%s";

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    private ServiceURIResolver uriResolver;

    public HTTPCourseReservesService(final ObjectMapper objectMapper, final URI catalogServiceURI,
            final ServiceURIResolver uriResolver) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
        this.uriResolver = uriResolver;
    }

    @Override
    public List<Course> getCourses() {
        try (InputStream input = this.uriResolver
                .getInputStream(this.catalogServiceURI.resolve(COURSES_ENDPOINT_PATH))) {
            return this.objectMapper.readValue(input, new TypeReference<List<Course>>() {
            });
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public CourseReservesItemList getItems() {
        try (InputStream input = this.uriResolver.getInputStream(this.catalogServiceURI.resolve(ITEMS_ENDPOINT_PATH))) {
            return this.objectMapper.readValue(input, CourseReservesItemList.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public CourseReservesItemList getItems(final int id) {
        String pathWithIDParam = String.format(ITEMS_BY_ID_ENDPOINT_PATH_FORMAT, id);
        try (InputStream input = this.uriResolver.getInputStream(this.catalogServiceURI.resolve(pathWithIDParam))) {
            return this.objectMapper.readValue(input, CourseReservesItemList.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
