package org.assured.project.api.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigRep {

    public static class API  {
        public final static ApiConfig DEFAULT_CONFIG = ConfigFactory.create(ApiConfig.class);
    }

}
