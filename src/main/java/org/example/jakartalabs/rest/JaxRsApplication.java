package org.example.jakartalabs.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    // Jersey автоматично сканує пакет і знаходить усі @Path-ресурси
}
