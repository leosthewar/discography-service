package com.clara.discographyservice.infrastructure.config;

import com.clara.discographyservice.application.domain.model.artist.ArtistDeserializer;
import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;
import com.clara.discographyservice.application.domain.model.release.ReleaseDeserializer;
import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ObjectMapperConfig {

    @Bean
    @Scope("prototype")
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ArtistEntity.class, new ArtistDeserializer());
        module.addDeserializer(ReleaseEntity.class, new ReleaseDeserializer());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(module);
        return mapper;
    }
}
