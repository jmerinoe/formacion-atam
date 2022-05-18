// <reference types="cypress" />

context('Actions', () => {
  before(() => {
    cy.task('setupConfigFiles');
  });

  beforeEach(() => {
    cy.visit('/');
  });

  afterEach(function () {
    console.log(this.currentTest.title);
    console.log(this.currentTest.state);

    const result = {
      testName: this.currentTest.title,
      runStatus: this.currentTest.state
    };
    cy.task('writeResult', result);
  });

  describe('Regression Tests', () => {

    it('.ATAM_REGTC01 - Aceptar cookies', () => {
      cy.get('div.cookie-notice-container').should('be.visible');
      cy.get('#cn-accept-cookie').click();
      cy.get('div.cookie-notice-container').should('not.be.visible');
    });

    it('.ATAM_REGTC02 - Cierre de overlay cookies', () => {
      cy.closeCookiesBanner();
    });

    it('.ATAM_REGTC03 - Comportamiento boton cargar mas noticias', () => {
      cy.closeCookiesBanner();
      cy.loadMoreNewsRecursive();
    });

    it('.ATAM_REGTC04 - Interceptando peticion noticias', () => {
      cy.closeCookiesBanner();
      cy.loadMoreNews();
    });
  });
});
