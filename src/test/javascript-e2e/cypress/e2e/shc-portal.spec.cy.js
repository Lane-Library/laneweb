describe('Lane Search Test Case', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/portals/shc.html');
        cy.get('.verticalPico input[name=p]').as('patientCondition');
        cy.get('.verticalPico input[name=i]').as('intervention');
        cy.get('.verticalPico input[name=c]').as('comparison');
        cy.get('.verticalPico input[name=o]').as('outcome');
    });

    it('suggestion should not exist on loading page', () => {
        cy.get('.aclist-item').should('not.exist');
    })

    it('suggestion should exist after typing 3 characters in patientCondition input', () => {
        cy.get('@patientCondition').type('ski');
        cy.get('.aclist-item').should('exist');
        //count how many suggestions
        cy.get('.aclist-item').should('have.length', 10);
    })

    it('suggestion should exist after typing 3 characters in intervention input', () => {
        cy.get('@intervention').type('ski');
        cy.get('.aclist-item').should('exist');
    })

    it('suggestion should exist after typing 3 characters in comparison input', () => {
        cy.get('@comparison').type('ski');
        cy.get('.aclist-item').should('exist');
    })

    it('suggestion comparison should exist after typing 3 characters in outcome input', () => {
        cy.get('@outcome').type('ski');
        cy.get('.aclist-item').should('exist');
    })

    it('check generated url after submitting the form', () => {
        cy.get('@patientCondition').type('condition');
        cy.get('@intervention').focus();
        cy.get('@intervention').type('intervention');
        cy.get('@comparison').focus();
        cy.get('@comparison').type('comparison');
        cy.get('@outcome').focus();
        cy.get('@outcome').type('outcome');
        cy.wait(100);
        cy.get('@patientCondition').click();
        cy.get('.verticalPico .btn').click();
        cy.url().should('include', 'p=condition&i=intervention&c=comparison&o=outcome&q=%28condition%29+AND+%28intervention%29+AND+%28comparison%29+AND+%28outcome%29');
    });

    it('check google analytics  url', () => {
        // intercept the GA request and count the number of descriptionTrigger click events
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*en=page_view*').as('gaCollect');
        cy.get('@patientCondition').type('condition');
        cy.wait(100);
        cy.get('@patientCondition').click();
        cy.get('.verticalPico .btn').click();
        cy.wait('@gaCollect').then((interception) => {
            expect(interception.request.url).to.include('dl=%2FONSITE%2FSHC-Epic%20VerticalPico%20Search%2F%2Fsearch.html');
        });
    });

});