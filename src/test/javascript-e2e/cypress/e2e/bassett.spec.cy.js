describe('Bassett', () => {

    it('displays and hides the tablet Bassett collection menu', () => {
        // default viewport is 1000px x 660px
        cy.visit('/biomed-resources/bassett/index.html');
        cy.get('h2').as('collectionHome');
        cy.get('#abdomen a.see-all').as('abdomenSeeAll');

        cy.get('@collectionHome').should('be.visible');
        cy.get('@abdomenSeeAll').should('not.be.visible');

        cy.get('@collectionHome').click();
        cy.get('@abdomenSeeAll').should('be.visible');
    })

    it('displays and hides the Abdomen See All menu', () => {
        cy.viewport(1101, 750);
        cy.visit('/biomed-resources/bassett/index.html');

        cy.get('#abdomen a.see-all').as('abdomenSeeAll');

        cy.get('#abdomen li').should('have.length.gt', 10);
        cy.get('#abdomen li:visible').should('have.length.lt', 10);

        cy.get('@abdomenSeeAll').click();
        cy.get('#abdomen li:visible').should('have.length.gt', 10);

        cy.get('@abdomenSeeAll').click();
        cy.get('#abdomen li:visible').should('have.length.lt', 10);
    })

    // more tests needed
})