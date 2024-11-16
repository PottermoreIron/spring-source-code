package com.pot.c4;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Pot
 * @created: 2024-09-18 22:37
 * @description: bean4
 */

@ConfigurationProperties(prefix = "java")
public class Bean4 {
    private String home;
    private String version;


    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Bean4{" +
                "home='" + home + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
