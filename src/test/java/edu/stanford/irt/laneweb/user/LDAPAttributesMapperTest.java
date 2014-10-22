package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.LDAPAttributesMapper;
import edu.stanford.irt.laneweb.user.LDAPData;

public class LDAPAttributesMapperTest {

    private Attribute attribute;

    private Attributes attributes;

    @SuppressWarnings("rawtypes")
    private NamingEnumeration enumeration;

    private LDAPAttributesMapper mapper;

    @Before
    public void setUp() throws Exception {
        this.mapper = new LDAPAttributesMapper(Collections.singleton("stanford:staff"));
        this.attributes = createMock(Attributes.class);
        this.attribute = createMock(Attribute.class);
        this.enumeration = createMock(NamingEnumeration.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFromAttributes() throws NamingException {
        expect(this.attributes.get("displayName")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("name");
        expect(this.attributes.get("uid")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("uid");
        expect(this.attributes.get("suunivid")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("suunivid");
        expect(this.attributes.get("suAffiliation")).andReturn(this.attribute);
        expect(this.attribute.getAll()).andReturn(this.enumeration);
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("");
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("stanford:staff");
        expect(this.attributes.get("mail")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("mail");
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = (LDAPData) this.mapper.mapFromAttributes(this.attributes);
        assertEquals("name", data.getName());
        assertEquals("uid", data.getSunetId());
        assertEquals("suunivid", data.getUnivId());
        assertTrue(data.isActive());
        assertEquals("mail", data.getEmailAddress());
        verify(this.attributes, this.attribute, this.enumeration);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFromAttributesNotActive() throws NamingException {
        expect(this.attributes.get("displayName")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("name");
        expect(this.attributes.get("uid")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("uid");
        expect(this.attributes.get("suunivid")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("suunivid");
        expect(this.attributes.get("suAffiliation")).andReturn(this.attribute);
        expect(this.attribute.getAll()).andReturn(this.enumeration);
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("");
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("foo");
        expect(this.enumeration.hasMore()).andReturn(false);
        expect(this.attributes.get("mail")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("mail");
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = (LDAPData) this.mapper.mapFromAttributes(this.attributes);
        assertEquals("name", data.getName());
        assertEquals("uid", data.getSunetId());
        assertEquals("suunivid", data.getUnivId());
        assertFalse(data.isActive());
        assertEquals("mail", data.getEmailAddress());
        verify(this.attributes, this.attribute, this.enumeration);
    }

    @Test
    public void testMapFromAttributesNullAttributes() throws NamingException {
        expect(this.attributes.get("displayName")).andReturn(null);
        expect(this.attributes.get("uid")).andReturn(null);
        expect(this.attributes.get("suunivid")).andReturn(null);
        expect(this.attributes.get("suAffiliation")).andReturn(null);
        expect(this.attributes.get("mail")).andReturn(null);
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = (LDAPData) this.mapper.mapFromAttributes(this.attributes);
        assertNull(data.getName());
        assertNull(data.getSunetId());
        assertNull(data.getUnivId());
        assertFalse(data.isActive());
        assertNull(data.getEmailAddress());
        verify(this.attributes, this.attribute, this.enumeration);
    }
}
