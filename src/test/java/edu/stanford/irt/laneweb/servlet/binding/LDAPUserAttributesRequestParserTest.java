package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.UserAttribute;

public class LDAPUserAttributesRequestParserTest {

    private LDAPData data;

    private LDAPDataAccess ldap;

    private LDAPUserAttributesRequestParser parser;

    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.data = createMock(LDAPData.class);
        this.request = createMock(HttpServletRequest.class);
        this.ldap = createMock(LDAPDataAccess.class);
        this.parser = new LDAPUserAttributesRequestParser(this.ldap);
    }

    @Test
    public void testParse() {
        expect(this.request.getRemoteUser()).andReturn("id");
        expect(this.ldap.getLdapDataForSunetid("id")).andReturn(this.data);
        expect(this.data.getEmailAddress()).andReturn("email");
        expect(this.data.getName()).andReturn("name");
        expect(this.data.getSunetId()).andReturn("id");
        expect(this.data.isActive()).andReturn(true);
        expect(this.data.getUnivId()).andReturn("univId");
        replay(this.ldap, this.request, this.data);
        Map<UserAttribute, String> attributes = this.parser.parse(this.request);
        assertEquals("id", attributes.get(UserAttribute.ID));
        assertEquals("univId", attributes.get(UserAttribute.UNIV_ID));
        assertEquals("name", attributes.get(UserAttribute.NAME));
        assertEquals("true", attributes.get(UserAttribute.ACTIVE));
        assertEquals("email", attributes.get(UserAttribute.EMAIL));
        assertEquals("stanford.edu", attributes.get(UserAttribute.PROVIDER));
        verify(this.ldap, this.request, this.data);
    }

    @Test(expected = LanewebException.class)
    public void testParseNullRemoteUser() {
        expect(this.request.getRemoteUser()).andReturn(null);
        replay(this.ldap, this.request, this.data);
        this.parser.parse(this.request);
    }
}
