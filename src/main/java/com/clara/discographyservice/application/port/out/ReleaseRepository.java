package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;

public interface ReleaseRepository {
    ReleaseEntity save(ReleaseEntity release);
}
