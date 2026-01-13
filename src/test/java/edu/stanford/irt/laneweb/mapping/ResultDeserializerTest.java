package edu.stanford.irt.laneweb.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.Version;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ResultDeserializerTest {

    private JsonMapper objectMapper;

    @BeforeEach
    public void setUp() {
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Result.class, new ResultDeserializer());
        this.objectMapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    public void testDescribePubMedDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("describe-pubmed.json"),
                Result.class);

        assertNotNull(result);
        assertEquals("631585061", result.getId());
        assertEquals("Lane Metasearch - Version:1.2.92-SNAPSHOT", result.getDescription());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertNull(result.getStatus());
        assertNull(null, result.getTime());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://www.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&tool=stanfordmeta&email=ceyates@stanford.edu&retmax=50&term=rubella",
                    child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("-1", child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertNull(child.getStatus());
            assertEquals("-1", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("pubmed", grandchild.getId());
                assertEquals("PubMed", grandchild.getDescription());
                assertEquals(
                        "https://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=search&term=rubella&holding=f1000%2CF1000M&otool=Stanford",
                        grandchild.getURL());
                assertEquals(0, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("0", grandchild.getHits());
                assertEquals("rubella", grandchild.getQuery().getSearchText());
                assertNull(grandchild.getStatus());
            }
        }
    }

    @Test
    public void testSearchAAFPSuccessfulDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-aafp_patients.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("631585061", result.getId());
        assertEquals("Lane Metasearch - Version:1.2.92-SNAPSHOT", result.getDescription());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("lupus", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
        for (Result child : result.getChildren()) {
            assertEquals("aafp_patients", child.getId());
            assertEquals("AAFP familydoctor.org", child.getDescription());
            assertEquals("https://www.aafp.org/global-search.html?#q=lupus&t=All&sort=relevancy", child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("417", child.getHits());
            assertEquals("lupus", child.getQuery().getSearchText());
            assertEquals(SearchStatus.SUCCESSFUL, child.getStatus());
            assertEquals("8648", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("aafp_patients", grandchild.getId());
                assertEquals("AAFP familydoctor.org", grandchild.getDescription());
                assertEquals("https://www.aafp.org/global-search.html?#q=lupus&t=All&sort=relevancy",
                        grandchild.getURL());
                assertEquals(10, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("417", grandchild.getHits());
                assertEquals("lupus", grandchild.getQuery().getSearchText());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                assertEquals(child.getTime(), grandchild.getTime());
                ContentResult content = (ContentResult) grandchild.getChildren().stream().reduce((a, b) -> b)
                        .orElse(null);
                assertNull(content.getAuthor());
                assertNull(content.getContentId());
                assertEquals(196, content.getDescription().length());
                assertEquals("content-9", content.getId());
                assertEquals("", content.getPublicationText());
            }
        }
    }

    @Test
    public void testSearchMMBIDDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-mmbid.json"), Result.class);
        assertNotNull(result);
        assertEquals("631585061", result.getId());
        assertEquals("Lane Metasearch - Version:1.2.92-SNAPSHOT", result.getDescription());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
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
                assertEquals("rubella", grandchild.getQuery().getSearchText());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                assertEquals(child.getTime(), grandchild.getTime());
            }
        }
    }

    @Test
    public void testSearchPubMedRunningDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-pubmed-running.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("631585061", result.getId());
        assertEquals("Lane Metasearch - Version:1.2.92-SNAPSHOT", result.getDescription());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.RUNNING, result.getStatus());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://www.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&tool=stanfordmeta&email=ceyates@stanford.edu&retmax=50&term=rubella",
                    child.getURL());
            assertEquals(0, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("-1", child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertEquals(SearchStatus.RUNNING, child.getStatus());

        }
    }

    @Test
    public void testSearchPubMedSuccessfulDeserialize() throws IOException {
        Result result = this.objectMapper.readValue(getClass().getResourceAsStream("search-pubmed-successful.json"),
                Result.class);
        assertNotNull(result);
        assertEquals("1363428449", result.getId());
        assertEquals("Lane Metasearch - Version:1.2.92-SNAPSHOT", result.getDescription());
        assertEquals(1, result.getChildren().size());
        assertNull(result.getException());
        assertNull(result.getHits());
        assertEquals("rubella", result.getQuery().getSearchText());
        assertEquals(SearchStatus.SUCCESSFUL, result.getStatus());
        for (Result child : result.getChildren()) {
            assertEquals("pubmed", child.getId());
            assertEquals("PubMed", child.getDescription());
            assertEquals(
                    "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&email=alainb@stanford.edu&api_key=a2aead2fd50d6d10a8bb79a981b0b6cfb209&retmode=xml&term=rubella",
                    child.getURL());
            assertEquals(1, child.getChildren().size());
            assertNull(child.getException());
            assertEquals("18280", child.getHits());
            assertEquals("rubella", child.getQuery().getSearchText());
            assertEquals(SearchStatus.SUCCESSFUL, child.getStatus());
            assertEquals("983", child.getTime());
            for (Result grandchild : child.getChildren()) {
                assertEquals("pubmed", grandchild.getId());
                assertEquals("PubMed", grandchild.getDescription());
                assertEquals(
                        "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&email=alainb@stanford.edu&retmode=xml&id=rubella",
                        grandchild.getURL());
                assertEquals(20, grandchild.getChildren().size());
                assertNull(grandchild.getException());
                assertEquals("18280", grandchild.getHits());
                assertEquals("rubella", grandchild.getQuery().getSearchText());
                assertEquals(SearchStatus.SUCCESSFUL, grandchild.getStatus());
                ContentResult content = (ContentResult) grandchild.getChildren().stream().findFirst().orElse(null);
                assertEquals("Hajipour N, Mohammady E, Barzegar G.", content.getAuthor());
                assertEquals("PMID:40963106", content.getContentId());
                assertEquals(1809, content.getDescription().length());
                assertEquals("content-0", content.getId());
                assertEquals("BMC Infect Dis. 2025 Sep 17;25(1)", content.getPublicationText());
            }
        }
    }
}
