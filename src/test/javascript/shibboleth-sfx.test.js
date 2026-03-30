describe('Shibboleth/SFX', () => {
    let html;
    const document = window.document;

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
        jest.isolateModules(() => {
            const { applyShibbolethSfx } = require('@/shibboleth-sfx.js');

            // Simulate iframe: self !== top
            applyShibbolethSfx(document, { self: {}, top: {} });
        });

        const links = document.querySelectorAll('#shibboleth-links a');
        expect(links[0].target).toBe('_blank');
        expect(links[1].target).toBe('_blank');
        expect(links[2].target).toBe('_blank');
        expect(links[3].target).toBe('');
    });

    it('should not set target="_blank" for matching links when not in an iframe', () => {
        jest.isolateModules(() => {
            const { applyShibbolethSfx } = require('@/shibboleth-sfx.js');

            // Not in iframe: self === top
            const same = {};
            applyShibbolethSfx(document, { self: same, top: same });
        });

        document.querySelectorAll('#shibboleth-links a').forEach(link => {
            expect(link.target).toBe('');
        });
    });
});