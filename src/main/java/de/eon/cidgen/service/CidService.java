package de.eon.cidgen.service;

import de.eon.cidgen.dto.CidRequest;
import de.eon.cidgen.entity.CidCounter;
import de.eon.cidgen.entity.GeneratedCid;
import de.eon.cidgen.repository.CidCounterRepository;
import de.eon.cidgen.repository.GeneratedCidRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * Creates CIDs and stores both their counter and audit record in PostgreSQL.
 */
@Service
public class CidService {

    public static final String BASE34 = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final long MAX_COUNTER = 1_336_335L;
    private static final Logger LOGGER = Logger.getLogger(CidService.class.getName());

    private final CidCounterRepository cidCounterRepository;
    private final GeneratedCidRepository generatedCidRepository;
    private final String rangeLetter;

    public CidService(CidCounterRepository cidCounterRepository,
                      GeneratedCidRepository generatedCidRepository,
                      @Value("${cid.range-letter}") String rangeLetter) {
        this.cidCounterRepository = cidCounterRepository;
        this.generatedCidRepository = generatedCidRepository;
        this.rangeLetter = validateRangeLetter(rangeLetter);
    }

    /**
     * A transaction makes the counter update and audit record one all-or-nothing operation.
     */
    @Transactional
    public String generateCid(CidRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        CidCounter cidCounter = cidCounterRepository.findByRangeLetterForUpdate(rangeLetter);

        if (cidCounter == null) {
            throw new IllegalStateException("No counter exists for CID range " + rangeLetter);
        }

        long currentCounter = cidCounter.getCounter();
        if (currentCounter < 0 || currentCounter > MAX_COUNTER) {
            throw new CidRangeExhaustedException(rangeLetter);
        }

        String cid = "C" + rangeLetter + toBase34(currentCounter);
        Instant generationTime = Instant.now();

        generatedCidRepository.save(new GeneratedCid(
                cid,
                request.getHrSystem(),
                request.getEonAccountingAreaID(),
                request.getEmployeeNumber(),
                generationTime
        ));
        cidCounter.increaseCounter();
        cidCounterRepository.save(cidCounter);

        LOGGER.info("Generated CID: " + cid + " at " + generationTime);
        return cid;
    }

    public String toBase34(long counter) {
        if (counter < 0 || counter > MAX_COUNTER) {
            throw new IllegalArgumentException("counter must be between 0 and " + MAX_COUNTER);
        }

        char[] result = {'0', '0', '0', '0'};
        long remaining = counter;
        for (int position = result.length - 1; position >= 0; position--) {
            int remainder = (int) (remaining % BASE34.length());
            result[position] = BASE34.charAt(remainder);
            remaining = remaining / BASE34.length();
        }
        return new String(result);
    }

    private String validateRangeLetter(String configuredRangeLetter) {
        if (configuredRangeLetter == null || !configuredRangeLetter.matches("[A-Z]")) {
            throw new IllegalArgumentException("cid.range-letter must be one uppercase letter from A to Z");
        }
        return configuredRangeLetter;
    }
}
