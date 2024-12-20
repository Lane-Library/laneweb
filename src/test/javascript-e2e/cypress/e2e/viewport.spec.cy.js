describe('Viewport Tests', () => {

    beforeEach(() => {
        cy.visit('cypress-test//index.html');

        // fire viewport:scrolled so we can put attach viewport to window
        cy.scrollTo(1, 1);
        cy.scrollTo(1, 1);

        cy.window().then((win) => {
            win.viewport = {};

            console.log('event ON');

            // win.L.on("viewport:init", function (event) {
            //     viewport = event.viewport;
            // });
            win.L.on("viewport:scrolled", function (event) {
                win.L.on("viewport:scrolled", function (event) {
                    win.viewport = event.viewport;
                });
            });
        });

        it('viewport exists', () => {
            cy.window().its('viewport').should('exist');
        });

        it('in viewport', () => {
            cy.get('header .brand').then($el => {
                cy.window().its('viewport').invoke('inView', $el[0]).should('be.true');
            });
        });

        it('not in viewport', () => {
            cy.get('footer .brand').then($el => {
                cy.window().its('viewport').invoke('inView', $el[0]).should('be.false');
            });
        });

        it('scroll', () => {
            cy.window().scrollTo('bottom');
            cy.get('footer .brand').then($el => {
                cy.window().its('viewport').invoke('inView', $el[0]).should('be.true');
            });
        });

    });