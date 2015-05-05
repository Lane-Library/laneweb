package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EresourcesCollectionManagerTest {

    private AbstractCollectionManager manager;

    private ResultSet resultSet;

    @Before
    public void setUp() throws Exception {
        this.manager = new EresourcesCollectionManager(null, null, null);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testInvalid() throws SQLException {
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getInt("RECORD_ID")).andReturn(1);
        expect(this.resultSet.getString("RECORD_TYPE")).andReturn("type1");
        expect(this.resultSet.getString("TITLE")).andReturn("title1");
        expect(this.resultSet.getInt("TOTAL")).andReturn(10);
        expect(this.resultSet.getInt("AVAILABLE")).andReturn(5);
        expect(this.resultSet.getString("DESCRIPTION")).andReturn("description1");
        expect(this.resultSet.getString("PRIMARY_TYPE")).andReturn("primaryType1");
        expect(this.resultSet.getInt("VERSION_ID")).andReturn(1);
        expect(this.resultSet.getString("GETPASSWORD")).andReturn("F");
        expect(this.resultSet.getInt("LINK_ID")).andReturn(1);
        expect(this.resultSet.getString("URL")).andReturn("url1");
        expect(this.resultSet.getString("LABEL")).andReturn("impact factor");
        expect(this.resultSet.getString("ADDITIONAL_TEXT")).andReturn("additional-text1");
        expect(this.resultSet.getString("HOLDINGS_DATES")).andReturn("holdings-dates");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.resultSet);
        List<Eresource> eresources = this.manager.parseResultSet(this.resultSet, null);
        assertTrue(eresources.isEmpty());
        verify(this.resultSet);
    }

    @Test
    public void testParseResultSet() throws SQLException {
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getInt("RECORD_ID")).andReturn(1);
        expect(this.resultSet.getString("RECORD_TYPE")).andReturn("type1");
        expect(this.resultSet.getString("TITLE")).andReturn("title1");
        expect(this.resultSet.getInt("TOTAL")).andReturn(10);
        expect(this.resultSet.getInt("AVAILABLE")).andReturn(5);
        expect(this.resultSet.getString("DESCRIPTION")).andReturn("description1");
        expect(this.resultSet.getString("PRIMARY_TYPE")).andReturn("primaryType1");
        expect(this.resultSet.getInt("VERSION_ID")).andReturn(1);
        expect(this.resultSet.getString("GETPASSWORD")).andReturn("T");
        expect(this.resultSet.getInt("LINK_ID")).andReturn(1);
        expect(this.resultSet.getString("URL")).andReturn("url1");
        expect(this.resultSet.getString("LABEL")).andReturn("label1");
        expect(this.resultSet.getString("ADDITIONAL_TEXT")).andReturn("additional-text1");
        expect(this.resultSet.getString("HOLDINGS_DATES")).andReturn("holdings-dates");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getInt("RECORD_ID")).andReturn(1);
        expect(this.resultSet.getString("RECORD_TYPE")).andReturn("type1");
        expect(this.resultSet.getString("TITLE")).andReturn("title1");
        expect(this.resultSet.getInt("VERSION_ID")).andReturn(2);
        expect(this.resultSet.getString("GETPASSWORD")).andReturn("F");
        expect(this.resultSet.getInt("LINK_ID")).andReturn(2);
        expect(this.resultSet.getString("LABEL")).andReturn("label2");
        expect(this.resultSet.getString("LINK_TEXT")).andReturn("link-text2");
        expect(this.resultSet.getString("URL")).andReturn("url2");
        expect(this.resultSet.getString("HOLDINGS_DATES")).andReturn("holdings-dates");
        expect(this.resultSet.getString("ADDITIONAL_TEXT")).andReturn("additional-text2");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(2);
        expect(this.resultSet.getInt("RECORD_ID")).andReturn(2);
        expect(this.resultSet.getString("RECORD_TYPE")).andReturn("type2");
        expect(this.resultSet.getString("TITLE")).andReturn("title2");
        expect(this.resultSet.getInt("TOTAL")).andReturn(0);
        expect(this.resultSet.getInt("AVAILABLE")).andReturn(0);
        expect(this.resultSet.getString("DESCRIPTION")).andReturn("description2");
        expect(this.resultSet.getString("PRIMARY_TYPE")).andReturn("primaryType2");
        expect(this.resultSet.getInt("VERSION_ID")).andReturn(3);
        expect(this.resultSet.getString("GETPASSWORD")).andReturn("F");
        expect(this.resultSet.getInt("LINK_ID")).andReturn(3);
        expect(this.resultSet.getString("LABEL")).andReturn("label3");
        expect(this.resultSet.getString("ADDITIONAL_TEXT")).andReturn("additional-text3");
        expect(this.resultSet.getString("URL")).andReturn("url3");
        expect(this.resultSet.getString("HOLDINGS_DATES")).andReturn("holdings-dates");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.resultSet);
        List<Eresource> eresources = this.manager.parseResultSet(this.resultSet, null);
        assertEquals(2, eresources.size());
        Eresource eresource = eresources.get(0);
        assertEquals(5, eresource.getAvailable());
        assertEquals("description1", eresource.getDescription());
        assertEquals("1", eresource.getId());
        assertEquals("primaryType1", eresource.getPrimaryType());
        assertEquals(1, eresource.getRecordId());
        assertEquals("type1", eresource.getRecordType());
        assertEquals(0, eresource.getScore(), 0);
        assertEquals("title1", eresource.getTitle());
        assertEquals(10, eresource.getTotal());
        Collection<Link> links = eresource.getLinks();
        Iterator<Link> it = links.iterator();
        assertEquals(2, links.size());
        Link link = it.next();
        assertEquals("additional-text1", link.getAdditionalText());
        assertEquals("label1", link.getLabel());
        assertEquals("title1", link.getLinkText());
        assertEquals(LinkType.GETPASSWORD, link.getType());
        assertEquals("url1", link.getUrl());
        link = it.next();
        assertEquals("additional-text2", link.getAdditionalText());
        assertEquals("label2", link.getLabel());
        assertEquals("link-text2", link.getLinkText());
        assertEquals(LinkType.NORMAL, link.getType());
        assertEquals("url2", link.getUrl());
        eresource = eresources.get(1);
        assertEquals(0, eresource.getAvailable());
        assertEquals("description2", eresource.getDescription());
        assertEquals("2", eresource.getId());
        assertEquals("primaryType2", eresource.getPrimaryType());
        assertEquals(2, eresource.getRecordId());
        assertEquals("type2", eresource.getRecordType());
        assertEquals(0, eresource.getScore(), 0);
        assertEquals("title2", eresource.getTitle());
        assertEquals(0, eresource.getTotal());
        links = eresource.getLinks();
        assertEquals(1, links.size());
        link = links.iterator().next();
        assertEquals("additional-text3", link.getAdditionalText());
        assertEquals("label3", link.getLabel());
        assertEquals("title2", link.getLinkText());
        assertEquals(LinkType.NORMAL, link.getType());
        assertEquals("url3", link.getUrl());
        verify(this.resultSet);
    }
}
