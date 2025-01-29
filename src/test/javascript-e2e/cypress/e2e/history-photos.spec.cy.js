// history photo loading behavior:
//  some images are embedded in the page
//  after the page loads, data for 6 images is fetched from server
//  after 15 seconds, the 6 images from the server are shown (brief opacity of 0 then 1)
//  images are resized to 200px height and aspect ratio
//  images are linked to the correct page
//  another server request is made after 15 seconds (untested)
describe('History Photos', () => {

    beforeEach(() => {
        cy.viewport(1101, 750);
        cy.intercept('GET', 'https://purl.stanford.edu/*', {
            fixture: 'history/history.jpg'
        })
    });


    it('should show embedded images after images load from server', () => {
        cy.intercept('GET', '/apps/getHistoryPhotoList', {
            fixture: 'history/history-photos.json'
        }).as('getHistoryPhotos');
        cy.visit('/med-history/index.html');

        cy.get('.history-photos a').should('have.length', 12);
        cy.wait('@getHistoryPhotos');
        cy.get('.history-photos').should('have.css', 'opacity', '1');
    })

    it('should show embedded images even after server error', () => {
        cy.intercept('GET', '/apps/getHistoryPhotoList', {
            statusCode: 500
        }).as('getHistoryPhotos');
        cy.visit('/med-history/index.html');
        cy.get('.history-photos a').should('have.length', 12);
        cy.wait('@getHistoryPhotos');
        cy.get('.history-photos').should('have.css', 'opacity', '1');
    })

    it('after 15s, should load images and update links', () => {
        cy.intercept('GET', '/apps/getHistoryPhotoList', {
            fixture: 'history/history-photos.json'
        }).as('getHistoryPhotos');
        cy.visit('/med-history/index.html');
        cy.wait('@getHistoryPhotos').then((interception) => {
            const photos = interception.response.body;
            cy.wait(15001);
            photos.slice(0, 6).forEach((photo, index) => {
                cy.get('.history-photos a').eq(index).should('have.attr', 'href', photo.page);
                cy.get('.history-photos a img').eq(index).should('have.css', 'height', '200px');
            });
        });
    });

});