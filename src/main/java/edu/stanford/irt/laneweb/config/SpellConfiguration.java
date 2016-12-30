package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.spell.PubmedESpellChecker;

@Configuration
public class SpellConfiguration {

    @Bean
    public PubmedESpellChecker spellChecker() {
        return new PubmedESpellChecker();
    }
}
