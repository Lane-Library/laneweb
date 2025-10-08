// minify-javascript.js
const fs = require('fs').promises;
const path = require('path');
const terser = require('terser');

//
// Configuration: Define bundles here in a clean, maintainable way.
// The file paths are relative to the project root.
//
const bundles = [
    {
        name: 'lane-all.min.js',
        output: 'target/classes/static/resources/javascript/lane-all.min.js',
        inputs: [
            'src/main/javascript/lane.js',
            'src/main/javascript/util.js',
            'src/main/javascript/menu.js',
            'src/main/javascript/mobile-navigation.js',
            'src/main/javascript/model.js',
            'src/main/javascript/lightbox.js',
            'src/main/javascript/guides.js',
            'src/main/javascript/link-info.js',
            'src/main/javascript/persistent-login.js',
            'src/main/javascript/search-indicator.js',
            'src/main/javascript/bassett.js',
            'src/main/javascript/suggest.js',
            'src/main/javascript/slideshow.js',
            'src/main/javascript/search.js',
            'src/main/javascript/search-reset.js',
            'src/main/javascript/search-dropdown.js',
            'src/main/javascript/search-placeholder.js',
            'src/main/javascript/search-help.js',
            'src/main/javascript/search-suggest.js',
            'src/main/javascript/search-pico-toggle.js',
            'src/main/javascript/search-pico-fields.js',
            'src/main/javascript/search-pico.js',
            'src/main/javascript/bookmark.js',
            'src/main/javascript/bookmarks.js',
            'src/main/javascript/bookmarks-widget.js',
            'src/main/javascript/bookmark-editor.js',
            'src/main/javascript/bookmarks-editor.js',
            'src/main/javascript/bookmark-animation.js',
            'src/main/javascript/bookmark-login.js',
            'src/main/javascript/bookmark-link.js',
            'src/main/javascript/tracking.js',
            'src/main/javascript/google-GA4.js',
            'src/main/javascript/popup.js',
            'src/main/javascript/tooltips.js',
            'src/main/javascript/spellcheck.js',
            'src/main/javascript/description-toggle.js',
            'src/main/javascript/shibboleth-sfx.js',
            'src/main/javascript/back-to-top.js',
            'src/main/javascript/solr-date-facet.js',
            'src/main/javascript/solr-facet-search.js',
            'src/main/javascript/solr-facet-suggest.js',
            'src/main/javascript/solr-pagination.js',
            'src/main/javascript/shc-portal.js',
            'src/main/javascript/bookcovers.js',
            'src/main/javascript/browzine.js',
            'src/main/javascript/viewport.js',
            'src/main/javascript/authors-toggle.js',
            'src/main/javascript/clinical-toggle.js',
            'src/main/javascript/search-form-scroll.js',
            'src/main/javascript/message.js',
            'src/main/javascript/tables-search.js',
            'src/main/javascript/permalink-toggle.js',
            'src/main/javascript/validation.js',
            'src/main/javascript/holdings-toggle.js',
            'src/main/javascript/table-hide-empty-columns.js',
            'src/main/javascript/zotero.js',
            'src/main/javascript/altmetric.js',
            'src/main/javascript/sfp-form.js',
            'src/main/javascript/history-photos.js'
        ]
    }
];

// Main build function
async function build() {
    console.log('Starting JavaScript minification...');
    for (const bundle of bundles) {
        try {
            // Use a map to read all files into an object { filename: code }
            const code = {};
            for (const file of bundle.inputs) {
                code[file] = await fs.readFile(file, 'utf8');
            }

            // Minify the code using Terser's programmatic API
            const result = await terser.minify(code, {
                compress: true,
                mangle: true,
            });

            if (result.error) {
                throw new Error(result.error);
            }

            // Ensure the output directory exists
            const outputDir = path.dirname(bundle.output);
            await fs.mkdir(outputDir, { recursive: true });

            // Write the minified code to the output file
            await fs.writeFile(bundle.output, result.code);
            console.log(`✅ Successfully created bundle: ${bundle.name}`);

        } catch (err) {
            console.error(`❌ Error creating bundle ${bundle.name}:`, err);
            process.exit(1); // Exit with an error code
        }
    }
    console.log('JavaScript minification complete.');
}

// Run the build
build();
