package edu.stanford.irt.laneweb.history;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class HistoryPhotoListCreatorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private HistoryPhotoListCreator creator;

    private String expected = "https://purl.stanford.edu/bk516vb8666\thttps://purl.stanford.edu/bk516vb8666.jpg\tEmblems and Insignia\n"
            + "https://purl.stanford.edu/qt402pj1793\thttps://purl.stanford.edu/qt402pj1793.jpg\tRoy Barnett Cohn with Rodney Rau Beard, David Glick, Sidney Raffel\n";

    private Object photo;

    @BeforeEach
    public void setUp() {
        this.creator = new HistoryPhotoListCreator(getClass().getResource("history-photos.json").toExternalForm());
        this.photo = mock(HistoryPhoto.class);
    }

    @Test
    public void testMain() {
        String[] args = { "file:/foo/hm.txt" };
        assertThrows(LanewebException.class, () -> {
            HistoryPhotoListCreator.main(args);
        });
    }

    @Test
    public void testPrintList() throws IOException {
        replay(this.photo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        this.creator.printList(pw);
        assertEquals(this.expected, baos.toString(UTF_8));
        verify(this.photo);
    }
}
