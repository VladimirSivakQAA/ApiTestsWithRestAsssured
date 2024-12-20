package org.assured.project.api.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/api.properties")
public interface ApiConfig extends Config {

    @Key("api.url")
    String url();
}
