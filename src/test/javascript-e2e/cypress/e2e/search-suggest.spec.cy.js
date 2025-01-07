describe('Suggest', () => {

    beforeEach(() => {
        cy.viewport(1200, 1000);
        cy.visit('/cypress-test/index.html');

        // Parameters
        cy.get('.hero-unit input[name=q]').as('input');

    })

    it('suggestion should not exist on loading page', () => {
        cy.get('.aclist-item').should('not.exist');
    })

    it('suggestion should not exist after typing 2 characters', () => {
        cy.get('@input').type('ca');
        cy.get('.aclist-item').should('not.exist');
    })


    it('suggestion should exist after typing 3 characters', () => {
        cy.get('@input').type('ski');
        cy.get('.aclist-item').should('exist');
        //count how many suggestions
        cy.get('.aclist-item').should('have.length', 10);
    })

    it('suggestion should not not found', () => {
        cy.intercept('/apps/suggest/getSuggestionList*', { fixture: 'suggest/suggest-not-found.json' }).as('suggestNoMatch');
        cy.get('@input').type('notFoundtest');
        cy.wait('@suggestNoMatch');
        cy.get('.aclist-item').should('not.exist');
    })

    //click on the suggest and check the input value
    it('click on suggestion', () => {
        cy.intercept('/apps/suggest/getSuggestionList*', { fixture: 'suggest/suggest.json' }).as('suggest10Match');
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*en=lane%3AsuggestSelect*').as('gaCollect');
        cy.get('@input').type('skin');
        cy.wait('@suggest10Match');
        //Click on Skin
        cy.get('.aclist-item').first().click();
        //Check if request sent to google analytics
        cy.wait('@gaCollect').then((interception) => {
            expect(interception.request.url).to.include('ep.event_label=Skin');
            expect(interception.request.url).to.include('ep.event_action=all-all');
        });
        //Check input value from the first suggestion
        cy.get('@input').should('have.value', 'Skin');
        //Check url after click
        cy.url().should('include', 'search.html?q=Skin');
        //check if request sent to google analytics 

    })

    //Test suggestion after the search input loosing the focus
    it('suggestion should not exist after loosing focus', () => {
        cy.get('@input').type('skin');
        cy.get('.aclist-item').should('exist');
        cy.get('#mc-embedded-subscribe').click();
        cy.get('.aclist-item').should('not.exist');
    })

    it('check keyboard selection for suggestion ', () => {
        cy.get('@input').type('skin');
        cy.get('.aclist-item').should('exist');
        cy.get('@input').type("{downArrow}");
        cy.get('@input').type("{downArrow}");
        cy.get('.aclist-item').first().should('have.class', 'aclist-item-active');
        cy.get('@input').type("{upArrow}");
        cy.get('.aclist-item').last().should('have.class', 'aclist-item-active');
    })




})