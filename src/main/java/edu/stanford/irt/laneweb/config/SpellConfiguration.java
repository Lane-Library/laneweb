package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.spell.PubmedESpellChecker;
import edu.stanford.irt.spell.SpellChecker;

@Configuration
public class SpellConfiguration {

    @Bean
    public SpellChecker spellChecker() {
        return new PubmedESpellChecker();
    }
}
