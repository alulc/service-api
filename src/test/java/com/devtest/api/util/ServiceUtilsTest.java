package com.devtest.api.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceUtilsTest {

    private ServiceUtils serviceUtils;

    @Before
    public void setup() {
        serviceUtils = new ServiceUtils();
    }

    @Test
    public void testBase64Encode() {
        String encoded = serviceUtils.base64Encode("0");
        assertEquals("MA==", encoded);
    }

    @Test
    public void testBase64DecodeSuccess() {
        Optional<String> decoded = serviceUtils.base64Decode("MA==");
        assertTrue(decoded.isPresent());
        assertEquals("0", decoded.get());
    }

    @Test
    public void testBase64DecodeFail() {
        Optional<String> decoded = serviceUtils.base64Decode("_");
        assertFalse(decoded.isPresent());
    }

    @Test
    public void testToLongSuccess() {
        Optional<Long> number = serviceUtils.toLong("0");
        assertTrue(number.isPresent());
        assertEquals(new Long(0), number.get());
    }

    @Test
    public void testToLongFail() {
        Optional<Long> number = serviceUtils.toLong("a");
        assertFalse(number.isPresent());
    }

    @Test
    public void testToUuidSuccess() {
        Optional<UUID> uuid = serviceUtils.toUuid(UUID.randomUUID().toString());
        assertTrue(uuid.isPresent());
    }

    @Test
    public void testToUuidFail() {
        Optional<UUID> uuid = serviceUtils.toUuid("a");
        assertFalse(uuid.isPresent());
    }
}