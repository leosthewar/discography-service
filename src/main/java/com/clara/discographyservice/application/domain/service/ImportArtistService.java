package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;
import com.clara.discographyservice.application.domain.model.artist.ArtistEntityMapper;
import com.clara.discographyservice.application.port.in.DeserializerDiscogsResponseException;
import com.clara.discographyservice.application.port.in.ImportArtistUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistGetQuery;
import com.clara.discographyservice.application.port.in.model.ArtistImportCommand;
import com.clara.discographyservice.application.port.in.model.ImportArtistResponse;
import com.clara.discographyservice.application.port.out.ArtistRepository;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
class ImportArtistService implements ImportArtistUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ImportArtistService.class);

    private final DiscogsAPIClient discogsAPIClient;
    private final ObjectMapper objectMapper;
    private final ArtistRepository artistRepository;

    public ImportArtistService(DiscogsAPIClient discogsAPIClient, ObjectMapper objectMapper,
                              ArtistRepository artistRepository) {
        this.discogsAPIClient = discogsAPIClient;
        this.objectMapper = objectMapper;
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional
    public Optional<ImportArtistResponse> importArtist(ArtistImportCommand importCommand) {
        // Validate if artist already exists in the database
        Optional<ArtistEntity> optionalArtist = artistRepository.findByDiscogsArtistId(importCommand.discogsArtistId());
        if (optionalArtist.isEmpty()) {
            // Artist does not exist in the database, Get artist from Discogs
            Optional<JsonNode> optionalJsonNode = discogsAPIClient.getArtist(new ArtistGetQuery(importCommand.discogsArtistId()));

            return optionalJsonNode.map(jsonNode -> {
                try {
                    // Deserialize the JsonNode into an Artist object
                    ArtistEntity artist = objectMapper.treeToValue(jsonNode, ArtistEntity.class);
                    // Save the artist to the database
                    artist = artistRepository.save(artist);
                    logger.info("Artist imported: {}", artist.getName());
                    return Optional.of(new ImportArtistResponse(ArtistEntityMapper.toReponseModel(artist), true));
                } catch (JsonProcessingException e) {
                    throw new DeserializerDiscogsResponseException("Error deserializing Discogs response", e);
                }
            }).orElse(Optional.empty());  // Return empty Optional if artist not found in Discogs
        }
        // Artist already exists in the database, return imported=false
        return Optional.of(new ImportArtistResponse(ArtistEntityMapper.toReponseModel(optionalArtist.get()), false));

    }
}
