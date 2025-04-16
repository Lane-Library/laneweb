describe('Shibboleth/SFX', () => {
    let html, document = window.document;

    beforeEach(() => {
        jest.resetModules();

        html = `
            <div id="shibboleth-links">
                <a href="https://idp.stanford.edu">Stanford IdP</a>
                <a href="https://adfs.stanfordmed.org">SHC ADFS</a>
                <a href="https://fs.stanfordchildrens.org">SCH ADFS</a>
                <a href="https://other.com">No _blank for me</a>
            </div>
        `;
        document.body.innerHTML = html;
    });

    it('should set target="_blank" for matching links when in an iframe', () => {

        // Mock window.self and window.top to simulate iframe
        Object.defineProperty(window, 'self', { value: 1 });
        Object.defineProperty(window, 'top', { value: 2 });

        require('@/shibboleth-sfx.js');

        const links = document.querySelectorAll('#shibboleth-links a');
        expect(links[0].target).toBe('_blank');
        expect(links[1].target).toBe('_blank');
        expect(links[2].target).toBe('_blank');
        expect(links[3].target).toBe('');
    });

    it('should not set target="_blank" for matching links when not in an iframe', () => {

        // set self and top properties to be equal to simulate not being in an iframe
        Object.defineProperty(window, 'self', { value: null });
        Object.defineProperty(window, 'top', { value: null });

        require('@/shibboleth-sfx.js');

        document.querySelectorAll('#shibboleth-links a').forEach(link => {
            expect(link.target).toBe('');
        });

    });

});

