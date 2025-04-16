package edu.stanford.irt.laneweb.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ResultDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Result.class, new ResultDeserializer());
        this.objectMapper.registerModule(module);
    }

    @Test
    public void testDescribePubMedDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("describe-pubmed.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("description", result.getId());
        assertEquals("description", result.getDescription());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertNull(result.getStatus());
        assertEquals("null", result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://www.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&tool=stanfordmeta&email=ceyates@stanford.edu&retmax=50&term=rubella",
                    child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertNull(child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertNull(child.getStatus());
            assertEquals(result.getTime(), child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("pubmed", grandchild.getId());
                assertEquals("PubMed", grandchild.getDescription());
                assertEquals(
                        "https://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=search&term=rubella&holding=f1000%2CF1000M&otool=Stanford",
                        grandchild.getURL());
                assertEquals(0, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertNull(grandchild.getHits());
                assertNull(grandchild.getQuery());
                assertNull(grandchild.getStatus());
                assertEquals(result.getTime(), grandchild.getTime());
            }
        }
    }

    @Test
    public void testSearchAAFPSuccessfulDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-aafp_patients.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("2069082541", result.getId());
        assertEquals("metasearch", result.getDescription());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("elephant", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
        assertEquals("null", result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("aafp_patients", child.getId());
            assertEquals("AAFP familydoctor.org", child.getDescription());
            assertEquals("http://s.aafp.org/?q=elephant&q1=&x1=", child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("29", child.getHits());
            assertEquals("elephant", child.getQuery().getSearchText());
            assertEquals(SearchStatus.SUCCESSFUL, child.getStatus());
            assertEquals("960", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("aafp_patients", grandchild.getId());
                assertEquals("AAFP familydoctor.org", grandchild.getDescription());
                assertEquals("http://s.aafp.org/?q=elephant&q1=&x1=", grandchild.getURL());
                assertEquals(25, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("29", grandchild.getHits());
                assertNull(grandchild.getQuery());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                assertEquals(result.getTime(), grandchild.getTime());
                ContentResult content = (ContentResult) grandchild.getChildren().stream().reduce((a, b) -> b)
                        .orElse(null);
                assertNull(content.getAuthor());
                assertNull(content.getContentId());
                assertEquals(13, content.getDescription().length());
                assertEquals("aafp_patients_content_25", content.getId());
                assertEquals("", content.getPublicationText());
            }
        }
    }

    @Test
    public void testSearchMMBIDDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-mmbid.json"), Result.class);
        assertNotNull(result);
        assertEquals("2069082541", result.getId());
        assertEquals("metasearch", result.getDescription());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
        assertEquals("null", result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("mmbid", child.getId());
            assertEquals("Metabolic & Molecular Bases of Inherited Disease", child.getDescription());
            assertEquals("http://ommbid.mhmedical.com/SearchResults.aspx?q=rubella", child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("12", child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertEquals(SearchStatus.SUCCESSFUL, child.getStatus());
            assertEquals("886", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("mmbid", grandchild.getId());
                assertEquals("Metabolic & Molecular Bases of Inherited Disease", grandchild.getDescription());
                assertEquals("http://ommbid.mhmedical.com/SearchResults.aspx?q=rubella", grandchild.getURL());
                assertEquals(0, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("12", grandchild.getHits());
                assertNull(grandchild.getQuery());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                assertEquals(result.getTime(), grandchild.getTime());
            }
        }
    }

    @Test
    public void testSearchPubMedRunningDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-pubmed-running.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("2069082541", result.getId());
        assertEquals("metasearch", result.getDescription());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.RUNNING, result.getStatus());
        assertEquals("null", result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://www.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&tool=stanfordmeta&email=ceyates@stanford.edu&retmax=50&term=rubella",
                    child.getURL());
            assertEquals(0, child.getChildren().size());
            assertNull(child.getException());
            assertNull(child.getHits());
            assertNull(child.getQuery());
            assertEquals(SearchStatus.RUNNING, child.getStatus());
            assertEquals(result.getTime(), child.getTime());
        }
    }

    @Test
    public void testSearchPubMedSuccessfulDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-pubmed-successful.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("2069082541", result.getId());
        assertEquals("metasearch", result.getDescription());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
        assertEquals("null", result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://www.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&tool=stanfordmeta&email=ceyates@stanford.edu&retmax=50&term=rubella",
                    child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("14462", child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertEquals(SearchStatus.SUCCESSFUL, child.getStatus());
            assertEquals("1521", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("pubmed", grandchild.getId());
                assertEquals("PubMed", grandchild.getDescription());
                assertEquals(
                        "https://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=search&term=rubella&holding=f1000%2CF1000M&otool=Stanford",
                        grandchild.getURL());
                assertEquals(50, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("14462", grandchild.getHits());
                assertNull(grandchild.getQuery());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                assertEquals(result.getTime(), grandchild.getTime());
                ContentResult content = (ContentResult) grandchild.getChildren().stream().findFirst().orElse(null);
                assertEquals("Nelly A, Marion LM, Lisa F, Pierre V, Céline P.", content.getAuthor());
                assertEquals("PMID:27599689", content.getContentId());
                assertEquals(1663, content.getDescription().length());
                assertEquals("pubmed_content_1", content.getId());
                assertEquals("Clin Microbiol Infect. 2016 Sep 3", content.getPublicationText());
            }
        }
    }
}
