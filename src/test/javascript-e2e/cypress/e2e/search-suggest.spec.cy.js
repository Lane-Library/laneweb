describe('Suggest', () => {

    beforeEach(() => {
        cy.viewport(1200, 1000);
        cy.visit('/cypress-test/index.html');

        // Parameters
        cy.get('.hero-unit input[name=q]').as('input');

    })

    it('suggestion should not exist on loading page', () => {
        cy.get('.yui3-aclist-item').should('not.exist');
    })

    it('suggestion should not exist after typing 2 characters', () => {
        cy.get('@input').type('ca');
        cy.get('.yui3-aclist-item').should('not.exist');
    })


    it('suggestion should exist after typing 3 characters', () => {
        cy.get('@input').type('ski');
        cy.get('.yui3-aclist-item').should('exist');
        //count how many suggestions
        cy.get('.yui3-aclist-item').should('have.length', 10);
    })

    it('suggestion should not not found', () => {
        cy.intercept('/apps/suggest/getSuggestionList*', { fixture: 'suggest-not-found.json' }).as('suggestNoMatch');
        cy.get('@input').type('notFoundtest');
        cy.wait('@suggestNoMatch');
        cy.get('.yui3-aclist-item').should('not.exist');
    })

    //click on the suggest and check the input value
    it('click on suggestion', () => {
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*en=lane%3AsuggestSelect*').as('gaCollect');
        cy.get('@input').type('skin');
        //Click on Skin
        cy.get('.yui3-aclist-item').first().click();
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
        cy.get('.yui3-aclist-item').should('exist');
        cy.get('#pubmed').focus();
        cy.get('.yui3-aclist-item').should('not.visible');
    })





})