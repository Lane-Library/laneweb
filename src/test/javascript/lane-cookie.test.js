require('@/lane.js');

describe('L.Cookie', () => {
    beforeEach(() => {
        // Clear all cookies before each test
        document.cookie.split(';').forEach(cookie => {
            const name = cookie.split('=')[0].trim();
            document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
        });
    });

    test('should set a cookie', () => {
        L.Cookie.set('testCookie', 'testValue', 1);
        expect(document.cookie).toContain('testCookie=testValue');
    });

    test('should get a cookie value by name', () => {
        document.cookie = 'testCookie=testValue';
        const value = L.Cookie.get('testCookie');
        expect(value).toBe('testValue');
    });

    test('should return undefined for a non-existent cookie', () => {
        const value = L.Cookie.get('nonExistentCookie');
        expect(value).toBeUndefined();
    });

    test('should remove a cookie', () => {
        document.cookie = 'testCookie=testValue';
        L.Cookie.remove('testCookie');
        expect(document.cookie).not.toContain('testCookie');
    });

    test('should set a cookie with an expiration date', () => {
        L.Cookie.set('expiringCookie', 'expiringValue', 1);
        const cookie = document.cookie.split(';').find(c => c.trim().startsWith('expiringCookie='));
        expect(cookie).toContain('expiringValue');
    });

    test('should overwrite an existing cookie', () => {
        L.Cookie.set('testCookie', 'initialValue', 1);
        L.Cookie.set('testCookie', 'newValue', 1);
        expect(L.Cookie.get('testCookie')).toBe('newValue');
    });
});
