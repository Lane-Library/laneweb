package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class IPGroupSelectorTest {

    private Model model;

    private IPGroupSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new IPGroupSelector();
        this.model = createMock(Model.class);
        this.selector.setModel(this.model);
    }

    @Test
    public void testSelectNotPAVA() {
        expect(this.model.getObject(Model.IPGROUP, IPGroup.class)).andReturn(IPGroup.OTHER);
        replay(this.model);
        assertFalse(this.selector.select("PAVA", null, null));
        verify(this.model);
    }

    @Test
    public void testSelectPAVA() {
        expect(this.model.getObject(Model.IPGROUP, IPGroup.class)).andReturn(IPGroup.PAVA);
        replay(this.model);
        assertTrue(this.selector.select("PAVA", null, null));
        verify(this.model);
    }
}
