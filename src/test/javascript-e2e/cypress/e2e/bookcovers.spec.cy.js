describe('Book Covers', () => {

    it('displays bookcover images after a search', () => {
        cy.viewport(1101, 750);

        cy.intercept('/apps/bookcovers*', { fixture: 'bookcovers/search.json' }).as('bookcoversReq');

        cy.visit('/cypress-test/search.html?q=id%3Abib-12+OR+id%3Abib-13+OR+id%3Abib-17&source=all-all&facets=recordType%3A"bib"&sort=title_sort+asc,year+desc');

        cy.get('li[data-sid=bib-17] .bookcover img').should('exist');
        cy.get('li[data-sid=bib-17] .bookcover img').should('have.attr', 'src').and('eq', '//fake/17.png');

        cy.scrollTo(0, 100);
        cy.wait('@bookcoversReq');
    })

    it('displays bookcover images from course reserves', () => {
        cy.viewport(1101, 750);

        cy.intercept('/apps/bookcovers*', { fixture: 'bookcovers/reserves.json' }).as('bookcoversReq');

        cy.visit('/cypress-test/biomed-resources/course-reserves-list.html?id=933b5def-cb4e-41a8-a5a1-a4ec1b6002e1');

        cy.get('div[data-bibid=327044]').should('exist');
        cy.get('div[data-bibid=327044] img').should('exist');
        cy.get('div[data-bibid=327044] img').should('have.attr', 'src').and('eq', '//fake/327044.png');

        cy.scrollTo(0, 100);
        cy.wait('@bookcoversReq');
    })

    it('does not request bookcover images for already loaded images', () => {
        cy.viewport(1101, 750);

        cy.intercept('/apps/bookcovers*', { fixture: 'bookcovers/search.json' }).as('bookcoversReq');

        cy.visit('/cypress-test/search.html?q=id%3Abib-17&source=all-all&facets=recordType%3A"bib"');

        cy.scrollTo(0, 100);
        cy.wait('@bookcoversReq');

        cy.get('li[data-sid=bib-17] .bookcover img').should('exist');
        cy.get('li[data-sid=bib-17] .bookcover img').should('have.attr', 'src').and('eq', '//fake/17.png');

        cy.get('@bookcoversReq.all').then((requests) => {
            // Assert that we have exactly one request so far.
            expect(requests).to.have.length(1);
        });

        // Scroll up and down to try to trigger more requests
        cy.scrollTo('bottom');
        // Add a small wait to ensure the scroll event is processed by the browser
        cy.wait(500);
        cy.scrollTo('top');
        cy.wait(500);
        cy.scrollTo('bottom');
        cy.wait(500);

        // Assert that NO new requests have been made
        cy.get('@bookcoversReq.all').should('have.length', 1);
    })

})