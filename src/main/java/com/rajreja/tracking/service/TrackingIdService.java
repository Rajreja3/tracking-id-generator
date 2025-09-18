package com.rajreja.tracking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TrackingIdService {
    private final long workerId;
    private final long epoch = 1672531200000L;
    private final int workerIdBits = 10;
    private final int sequenceBits = 12;
    private final long maxWorkerId = (1L << workerIdBits) - 1;
    private final long maxSequence = (1L << sequenceBits) - 1;

    private final long workerIdShift = sequenceBits;
    private final long timestampShift = sequenceBits + workerIdBits;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public TrackingIdService(@Value("${app.worker-id:0}") long workerIdEnv) {
        if (workerIdEnv < 0 || workerIdEnv > maxWorkerId) {
            throw new IllegalArgumentException("worker-id must be between 0 and " + maxWorkerId);
        }
        this.workerId = workerIdEnv;
    }

    // synchronized method to ensure thread-safety
    public synchronized long nextId() {
        long ts = currentTime();
        if (ts < lastTimestamp) {
            // clock moved backwards; refuse or wait
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id for " +
                    (lastTimestamp - ts) + "ms");
        }
        if (ts == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // exhausted sequence in this millisecond, wait for next millisecond
                ts = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = ts;
        return ((ts - epoch) << timestampShift) | (workerId << workerIdShift) | sequence;
    }

    private long waitNextMillis(long last) {
        long ts = currentTime();
        while (ts <= last) {
            ts = currentTime();
        }
        return ts;
    }

    private long currentTime() {
        return Instant.now().toEpochMilli();
    }

    /**
     * Produce an uppercase Base36 representation (0-9A-Z).
     * Caller can take substring/pad to ensure <=16 characters — usually length <= 13 for 63-bit numbers.
     */
    public String nextBase36() {
        long id = nextId();
        String s = Long.toString(id, 36).toUpperCase();
        if (s.length() > 16) {
            throw new IllegalStateException("Generated id is longer than 16 chars: " + s);
        }
        return s;
    }
}
