/** @type {import('jest').Config} */
const config = {
  verbose: true,
};

module.exports = {
  // Other Jest configuration options...
  moduleDirectories: ['node_modules', 'src/main/javascript'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/main/javascript/$1',
  },
  testEnvironment: 'jsdom'
};
