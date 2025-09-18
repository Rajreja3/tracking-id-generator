package com.rajreja.tracking.controller;

import com.rajreja.tracking.dto.TrackingResponse;
import com.rajreja.tracking.service.TrackingIdService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/tracking")
public class TrackingController {
    private final TrackingIdService idService;

    public TrackingController(TrackingIdService idService) {
        this.idService = idService;
    }

    @GetMapping("/next-tracking-number")
    public Object next(
            @RequestParam(name = "origin_country_id") String originCountryId,
            @RequestParam(name = "destination_country_id") String destinationCountryId,
            @RequestParam(name = "weight") String weight,
            @RequestParam(name = "created_at") String createdAt,
            @RequestParam(name = "customer_id") String customerId,
            @RequestParam(name = "customer_name") String customerName,
            @RequestParam(name = "customer_slug") String customerSlug
    ) {
        try {
            if (!originCountryId.matches("^[A-Z]{2}$") || !destinationCountryId.matches("^[A-Z]{2}$")) {
                return new org.springframework.http.ResponseEntity<>("Country codes must be ISO alpha-2 uppercase", HttpStatus.BAD_REQUEST);
            }
            if (!weight.matches("^\\d{1,3}(?:\\.\\d{1,3})?$")) {
                return new org.springframework.http.ResponseEntity<>("weight format invalid", HttpStatus.BAD_REQUEST);
            }
            // parse created_at (RFC3339)
            try {
                OffsetDateTime.parse(createdAt);
            } catch (DateTimeParseException ex) {
                return new org.springframework.http.ResponseEntity<>("created_at must be RFC3339", HttpStatus.BAD_REQUEST);
            }
            // check uuid
            try {
                UUID.fromString(customerId);
            } catch (IllegalArgumentException ex) {
                return new org.springframework.http.ResponseEntity<>("customer_id must be valid UUID", HttpStatus.BAD_REQUEST);
            }
            if (!customerSlug.matches("^[a-z0-9]+(?:-[a-z0-9]+)*$")) {
                return new org.springframework.http.ResponseEntity<>("customer_slug must be kebab-case lower-case", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new org.springframework.http.ResponseEntity<>("invalid parameters", HttpStatus.BAD_REQUEST);
        }

        String tracking = idService.nextBase36();
        String now = OffsetDateTime.now().toString();

        return new TrackingResponse(tracking, now);
    }
}
