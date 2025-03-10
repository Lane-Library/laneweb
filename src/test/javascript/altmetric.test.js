require('@/lane.js');
require('@/util.js');
require('@/model.js');

beforeEach(() => {
  // reset modules to ensure a clean state for Model, document, etc.
  jest.resetModules();

  // add .lwSearchResults div to the body
  const div = document.createElement('div');
  div.innerHTML = `
      <div class="lwSearchResults"></div>
    `;
  document.body.appendChild(div);

});

test('public, non-authenticated browser should only see dimensions badges', () => {
  L.Model.set(L.Model.IPGROUP, "OTHER");

  require('@/altmetric.js');

  const dimensionsScript = document.querySelector('script[src*="https://badge.dimensions.ai/badge.js"]'),
        altmetricsScript = document.querySelector('script[src*="https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js"]');

  expect(dimensionsScript).not.toBeNull();
  expect(altmetricsScript).toBeNull();
});

test('authenticated browser should see dimensions and altmetrics badges', () => {
  // override model to spoof authed user
  L.Model.set(L.Model.AUTH, "foo@test.org");

  require('@/altmetric.js');

  const dimensionsScript = document.querySelector('script[src*="https://badge.dimensions.ai/badge.js"]'),
        altmetricsScript = document.querySelector('script[src*="https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js"]');

  expect(dimensionsScript).not.toBeNull();
  expect(altmetricsScript).not.toBeNull();
});

test('on-campus browser should see dimensions and altmetrics badges', () => {
  // // override model to spoof on-campus user
  L.Model.set(L.Model.IPGROUP, "SU");

  require('@/altmetric.js');

  let dimensionsScript = document.querySelector('script[src*="https://badge.dimensions.ai/badge.js"]'),
        altmetricsScript = document.querySelector('script[src*="https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js"]');

  expect(dimensionsScript).not.toBeNull();
  expect(altmetricsScript).not.toBeNull();

});
