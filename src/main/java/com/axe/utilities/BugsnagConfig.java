package com.axe.utilities;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfig {
    @Bean
    public Bugsnag bugsnag() {
        return new Bugsnag("2647829ed5465e8368daabbea4b520b6");
    }

}
