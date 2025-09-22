describe('Search Form Scroll', () => {
    const SEARCH_PAGE_URL = '/cypress-test/search.html?source=all-all&q=ards';
    const SCROLL_OFFSET_DESKTOP = 130;
    const SCROLL_OFFSET_MOBILE = 30;

    beforeEach(() => {
        cy.visit(SEARCH_PAGE_URL);
    });

    it('should scroll the search form to the top on desktop', () => {
        cy.viewport(1101, 660);
        cy.get('form#search').then(($form) => {
            const formOffsetTop = $form[0].offsetTop;
            const expectedScrollY = formOffsetTop - SCROLL_OFFSET_DESKTOP;

            cy.scrollTo('top'); // Reset scroll position
            cy.reload(); // Reload the page to trigger the script
            cy.wait(100); // Wait for the scroll to complete

            cy.window().its('scrollY').should('equal', expectedScrollY);
        });
    });

    it('should scroll the search form to the top on mobile', () => {
        cy.viewport(375, 667);
        cy.get('form#search').then(($form) => {
            const formOffsetTop = $form[0].offsetTop;
            const expectedScrollY = formOffsetTop - SCROLL_OFFSET_MOBILE;

            cy.scrollTo('top'); // Reset scroll position
            cy.reload(); // Reload the page to trigger the script
            cy.wait(100); // Wait for the scroll to complete

            cy.window().its('scrollY').should('equal', expectedScrollY);
        });
    });

    it('should not scroll if already past the target position', () => {
        cy.viewport(1101, 768);
        cy.get('form#search').then(($form) => {
            const formOffsetTop = $form[0].offsetTop;
            const expectedScrollY = formOffsetTop - SCROLL_OFFSET_DESKTOP;

            cy.scrollTo(0, expectedScrollY + 20); // Scroll past the target position
            cy.reload(); // Reload the page to trigger the script
            cy.wait(100); // Wait for the scroll to complete

            console.log('Window scrollY:', window.scrollY);
            cy.window().its('scrollY').should('equal', expectedScrollY + 20);
        });
    });
});
