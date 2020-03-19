package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
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

public class LDAPAttributesMapperTest {

    private Attribute attribute;

    private Attributes attributes;

    @SuppressWarnings("rawtypes")
    private NamingEnumeration enumeration;

    private LDAPAttributesMapper mapper;

    @Before
    public void setUp() throws Exception {
        this.mapper = new LDAPAttributesMapper( Collections.singletonList("stanford:staff"));
        this.attributes = mock(Attributes.class);
        this.attribute = mock(Attribute.class);
        this.enumeration = mock(NamingEnumeration.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFromAttributes() throws NamingException {
        expect(this.attributes.get("uid")).andReturn(this.attribute);
        expect(this.attribute.get()).andReturn("uid");
        expect(this.attributes.get("suPrivilegeGroup")).andReturn(this.attribute);
        expect(this.attribute.getAll()).andReturn(this.enumeration);
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("");
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("stanford:staff");
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = this.mapper.mapFromAttributes(this.attributes);
        assertEquals("uid", data.getSunetId());
        assertTrue(data.isActive());
        verify(this.attributes, this.attribute, this.enumeration);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFromAttributesNotActive() throws NamingException {
        expect(this.attributes.get("suPrivilegeGroup")).andReturn(this.attribute);
        expect(this.attribute.getAll()).andReturn(this.enumeration);
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("");
        expect(this.enumeration.hasMore()).andReturn(true);
        expect(this.enumeration.next()).andReturn("foo");
        expect(this.enumeration.hasMore()).andReturn(false);
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = this.mapper.mapFromAttributes(this.attributes);
        assertNull(data.getSunetId());
        assertFalse(data.isActive());
        verify(this.attributes, this.attribute, this.enumeration);
    }

    @Test
    public void testMapFromAttributesNullAttributes() throws NamingException {
        expect(this.attributes.get("suPrivilegeGroup")).andReturn(null);
        replay(this.attributes, this.attribute, this.enumeration);
        LDAPData data = this.mapper.mapFromAttributes(this.attributes);
        assertNull(data.getSunetId());
        assertFalse(data.isActive());
        verify(this.attributes, this.attribute, this.enumeration);
    }
}
