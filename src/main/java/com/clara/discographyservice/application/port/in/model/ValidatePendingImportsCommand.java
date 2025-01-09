package com.clara.discographyservice.application.port.in.model;

import java.util.List;

public record ValidatePendingImportsCommand(
        List<Long> importIds) {

    public ValidatePendingImportsCommand {
        if(importIds == null || importIds.isEmpty()){
            throw new IllegalArgumentException("artistIds must not be null or empty");
        }
    }
}
