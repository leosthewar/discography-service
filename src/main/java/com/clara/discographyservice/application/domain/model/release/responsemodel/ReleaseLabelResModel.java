package com.clara.discographyservice.application.domain.model.release.responsemodel;


public record ReleaseLabelResModel(Long id, Long discogsLabelId, String name, String catno, String entityType,
                                   String entityTypeName, String discogsResourceUrl) {
}
