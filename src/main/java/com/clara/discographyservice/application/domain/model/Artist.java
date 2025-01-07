package com.clara.discographyservice.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Artist {

    private final Long id;
    private final Long discogsId;
    private final String name;
    private final String discogsResourceUrl;
    private final String discogsUri;
    private final String discogsReleasesUrl;
    private final String profile;
    private final Set<String> nameVariations;
    private final String dataQuality;
    private final List<ArtistImage> images;
    private final Set<String> urls;
    private final List<ArtistMember> members;

    public Artist(Long id, Long discogsId, String name, String discogsResourceUrl, String uri,
                  String discogsReleasesUrl, String profile, Set<String> nameVariations,
                  String dataQuality, List<ArtistImage> images, Set<String> urls,
                  List<ArtistMember> members) {

        if (discogsId == null || name == null) {
            throw new IllegalArgumentException("discogsId and name must not be null.");
        }

        this.id = id;
        this.discogsId = discogsId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri = uri;
        this.discogsReleasesUrl = discogsReleasesUrl;
        this.profile = profile;
        this.nameVariations = nameVariations != null ? new HashSet<>(nameVariations) : new HashSet<>();
        this.dataQuality = dataQuality;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.urls = urls != null ? new HashSet<>(urls) : new HashSet<>();
        this.members = members != null ? new ArrayList<>(members) : new ArrayList<>();
    }

    /**
     * Constructor to create an Artist with  null id
     *  It's use just to create an Artist to save in persistence
     */
    public Artist(Long discogsId, String name, String discogsResourceUrl, String uri, String discogsReleasesUrl, String profile, Set<String> nameVariations, String dataQuality, List<ArtistImage> images, Set<String> urls, List<ArtistMember> members) {
        this.id = null; // id will be assigned by the database
        this.discogsId = discogsId;
        this.name = name;
        this.discogsResourceUrl = discogsResourceUrl;
        this.discogsUri = uri;
        this.discogsReleasesUrl = discogsReleasesUrl;
        this.profile = profile;
        this.nameVariations = nameVariations;
        this.dataQuality = dataQuality;
        this.images = images;
        this.urls = urls;
        this.members = members;
    }

    // Helper methods to manage relationships

    public void addNameVariation(String nameVariation) {
        if (nameVariation != null && nameVariation.isBlank()) {
            throw new IllegalArgumentException("nameVariation must not be null or blank.");
        }
        nameVariations.add(nameVariation);
    }

    public void addUrl(String url) {
        if (url != null && url.isBlank()) {
            throw new IllegalArgumentException("url must not be null or blank.");
        }
        urls.add(url);
    }

    public void addImage(ArtistImage image) {
        if (image == null) {
            throw new IllegalArgumentException("image must not be null.");
        }
        images.add(image);
    }


    public void addMember(ArtistMember member) {
        if (member == null) {
            throw new IllegalArgumentException("member must not be null.");
        }
        members.add(member);
    }



}
