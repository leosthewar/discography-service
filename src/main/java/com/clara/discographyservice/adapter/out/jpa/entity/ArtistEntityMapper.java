package com.clara.discographyservice.adapter.out.jpa.entity;

import com.clara.discographyservice.application.domain.model.Artist;
import com.clara.discographyservice.application.domain.model.ArtistImage;
import com.clara.discographyservice.application.domain.model.ArtistMember;

import java.util.List;

public class ArtistEntityMapper {

    private ArtistEntityMapper() {

    }

    public static Artist toDomain(ArtistEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Artist(
                entity.getId(),
                entity.getDiscogsId(),
                entity.getName(),
                entity.getDiscogsResourceUrl(),
                entity.getDiscogsUri(),
                entity.getDiscogsReleasesUrl(),
                entity.getProfile(),
                entity.getNameVariations(),
                entity.getDataQuality(),
                toDomainImages(entity.getImages()),
                entity.getUrls(),
                toDomainMembers(entity.getMembers())
        );
    }

    public static ArtistEntity toEntity(Artist domain) {
        if (domain == null) {
            return null;
        }

        return new ArtistEntity(
                domain.getId(),
                domain.getDiscogsId(),
                domain.getName(),
                domain.getDiscogsResourceUrl(),
                domain.getDiscogsUri(),
                domain.getDiscogsReleasesUrl(),
                domain.getProfile(),
                domain.getNameVariations(),
                domain.getDataQuality(),
                toEntityImages(domain.getImages()),
                domain.getUrls(),
                toEntityMembers(domain.getMembers())
        );
    }

    private static List<ArtistImage> toDomainImages(List<ArtistImageEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                       .map(entity -> new ArtistImage(
                               entity.getId(),
                               entity.getType(),
                               entity.getDiscogsUri(),
                               entity.getDiscogsResourceUrl(),
                               entity.getDiscogsUri150(),
                               entity.getWidth(),
                               entity.getHeight()
                       ))
                       .toList();
    }

    private static List<ArtistImageEntity> toEntityImages(List<ArtistImage> domain) {
        if (domain == null) {
            return List.of();
        }

        return domain.stream()
                     .map(image -> new ArtistImageEntity(
                             image.getId(),
                             image.getType(),
                             image.getDiscogsUri(),
                             image.getDiscogsResourceUrl(),
                             image.getDiscogsUri150(),
                             image.getWidth(),
                             image.getHeight()
                     ))
                     .toList();
    }


    private static List<ArtistMember> toDomainMembers(List<ArtistMemberEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                       .map(entity -> new ArtistMember(
                               entity.getId(),
                               entity.getDiscogsArtistId(),
                               entity.getName(),
                               entity.getDiscogsResourceUrl(),
                               entity.getActive(),
                               entity.getThumbnailUrl()
                       ))
                       .toList();
    }

    private static List<ArtistMemberEntity> toEntityMembers(List<ArtistMember> domain) {
        if (domain == null) {
            return List.of();
        }

        return domain.stream()
                     .map(member -> new ArtistMemberEntity(
                             member.getId(),
                             member.getDiscogsArtistId(),
                             member.getName(),
                             member.getDiscogsResourceUrl(),
                             member.getActive(),
                             member.getThumbnailUrl()
                     ))
                     .toList();
    }
}