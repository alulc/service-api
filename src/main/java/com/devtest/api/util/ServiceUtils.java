package com.devtest.api.util;

import org.apache.commons.lang3.Validate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides simple helper functions.
 */
public class ServiceUtils {

    public ServiceUtils() {}

    public String base64Encode(String target) {

        Validate.notNull(target, "'target' cannot be null");

        String encoded = Base64.getEncoder().encodeToString(target.getBytes(StandardCharsets.UTF_8));
        return encoded;
    }

    public Optional<String> base64Decode(String target) {

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(target.getBytes(StandardCharsets.UTF_8));
            String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
            return Optional.of(decoded);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<Long> toLong(String target) {

        try {
            return Optional.of(Long.parseLong(target));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> toUuid(String target) {

        try {
            return Optional.of(UUID.fromString(target));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
