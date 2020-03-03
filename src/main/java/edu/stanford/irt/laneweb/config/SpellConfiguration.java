package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.spell.SpellChecker;
import edu.stanford.irt.spell.YandexSpellChecker;

@Configuration
public class SpellConfiguration {

    @Bean
    public SpellChecker spellChecker() {
        return new YandexSpellChecker();
    }
}
