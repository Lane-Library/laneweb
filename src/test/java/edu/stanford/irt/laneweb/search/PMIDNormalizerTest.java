/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author ryanmax
 */
public class PMIDNormalizerTest {

    @Test
    public void testNormalizeNonPmidStrings() {
        assertEquals(PMIDNormalizer.normalize("foo bar pmid"), "foo bar pmid");
        assertEquals(PMIDNormalizer.normalize("PMID: 2035728"), "PMID: 2035728");
        assertEquals(PMIDNormalizer.normalize("PMID:2035728"), "PMID:2035728");
        assertEquals(PMIDNormalizer.normalize("PMID 2035728"), "PMID 2035728");
        assertEquals(PMIDNormalizer.normalize("PMID2035728"), "PMID2035728");
        assertEquals(PMIDNormalizer.normalize("PMID#2035728"), "PMID#2035728");
        assertEquals(PMIDNormalizer.normalize("Ann Intern Med 142:560, 2005"), "Ann Intern Med 142:560, 2005");
        assertEquals(PMIDNormalizer.normalize("J Mol Med. 2010 May 28."), "J Mol Med. 2010 May 28.");
        assertEquals(PMIDNormalizer.normalize("# ISBN-13: 978-0763746575 "), "# ISBN-13: 978-0763746575 ");
        assertEquals(PMIDNormalizer.normalize("10.1080/15265160903316263"), "10.1080/15265160903316263");
        assertEquals(PMIDNormalizer.normalize("978-0865779204"), "978-0865779204");
        assertEquals(PMIDNormalizer.normalize("ncbiroflg15001385"), "ncbiroflg15001385");
    }

    @Test
    public void testNormalizePmidStrings() {
        assertEquals(PMIDNormalizer.normalize("PMID: 20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID:20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID 20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID#20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID #20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("PMID# 20357284"), "20357284");
        assertEquals(PMIDNormalizer.normalize("pubmed id 20609967"), "20609967");
        assertEquals(PMIDNormalizer.normalize("pubmed id: 20609967"), "20609967");
        assertEquals(PMIDNormalizer.normalize("pubmedid 20609967"), "20609967");
        assertEquals(PMIDNormalizer.normalize("pubmedid: 20609967"), "20609967");
        assertEquals(PMIDNormalizer.normalize("#20609967"), "20609967");
        assertEquals(PMIDNormalizer.normalize("Ann Intern Med 142:560, 2005 PMID: 15809467"),
                "Ann Intern Med 142:560, 2005 15809467");
        assertEquals(PMIDNormalizer.normalize("J Mol Med. 2010 May 28. PMID: 20508912"),
                "J Mol Med. 2010 May 28. 20508912");
    }
}
