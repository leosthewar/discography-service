package com.clara.discographyservice.application.port.in;

import java.util.List;

public record ValidatePendingImportsCommand(
        List<Long> importIds) {

    public ValidatePendingImportsCommand(List<Long> importIds ) {
        if(importIds == null || importIds.isEmpty()){
            throw new IllegalArgumentException("artistIds must not be null or empty");
        }
        this.importIds = importIds;
    }
}
