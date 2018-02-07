package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.spell.PubmedESpellChecker;
import edu.stanford.irt.spell.SpellChecker;

@Configuration
public class SpellConfiguration {

    @Bean
    public SpellChecker spellChecker(@Value("${edu.stanford.irt.laneweb.pubmed.apiKey}") final String pubmedApiKey) {
        return new PubmedESpellChecker(pubmedApiKey);
    }
}
