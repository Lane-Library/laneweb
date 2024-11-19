describe('Popup functionality', () => {

    it('should open a local popup from HTML found on page', () => {
        cy.visit('/patron-registration/index.html');

        // popup should not be present on the page initially
        cy.get('.popup').should('not.exist');

        cy.get('a[rel="popup local popup-id-shc"]').click();

        cy.get('.popup').should('exist');

        // verify popup is draggable
        cy.get('.popup').should('have.attr', 'draggable', 'true');

        cy.get('.popup .fa-close').click();

        // verify popup is closed
        cy.get('.popup').should('not.exist');

    });

    it('should open a new window with the correct URL; expect focus to be called on new window', () => {
        cy.visit('/biomed-resources/bassett/bassettView.html?bn=132-5');
        // test will fail unless we stub window.open and window.focus
        cy.window().then((win) => {
            cy.stub(win, 'open').as('windowOpen');
        });
        cy.get('@windowOpen').then((stub) => {
            const newWindow = { focus: cy.stub() };
            stub.returns(newWindow);
        });
        cy.get('#larger-view-link a').click();
        cy.get('@windowOpen')
            .should('be.calledWithMatch', /biomed-resources\/bassett\/raw\/bassettLargerView.html\?t=largerView&bn=132-5/,
                'newWin',
                'resizable,toolbar=no,location=no,scrollbars=yes,width=1320,height=1925');
    });

});