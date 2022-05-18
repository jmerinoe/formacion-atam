// in cypress/support/index.ts
// load type definitions that come with Cypress module
/// <reference types="cypress" />


// tslint:disable-next-line:no-namespace
declare namespace Cypress {

  interface Chainable {
    // home
    loadMoreNews(): void;
    loadMoreNewsRecursive(): void;
    numArticlesDisplayed(): Chainable<number>;
    closeCookiesBanner(): void;
  }
}
