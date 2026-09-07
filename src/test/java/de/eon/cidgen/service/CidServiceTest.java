package de.eon.cidgen.service;

import de.eon.cidgen.dto.CidRequest;
import de.eon.cidgen.entity.CidCounter;
import de.eon.cidgen.entity.GeneratedCid;
import de.eon.cidgen.support.TestRepositories;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CidServiceTest {

    @Test
    void convertsImportantBase34Boundaries() {
        CidService cidService = serviceWithCounter(0, new ArrayList<>());

        assertEquals("0000", cidService.toBase34(0));
        assertEquals("0001", cidService.toBase34(1));
        assertEquals("0009", cidService.toBase34(9));
        assertEquals("000A", cidService.toBase34(10));
        assertEquals("000Z", cidService.toBase34(33));
        assertEquals("0010", cidService.toBase34(34));
        assertEquals("0011", cidService.toBase34(35));
        assertEquals("ZZZZ", cidService.toBase34(CidService.MAX_COUNTER));
    }

    @Test
    void base34ValuesAlwaysHaveFourCharactersAndNeverUseIOrO() {
        String value = serviceWithCounter(0, new ArrayList<>()).toBase34(12_345);

        assertEquals(4, value.length());
        assertFalse(value.contains("I"));
        assertFalse(value.contains("O"));
    }

    @Test
    void generatesCidsFromZeroAndSavesTheContractAuditData() {
        List<GeneratedCid> savedCids = new ArrayList<>();
        CidCounter counter = new CidCounter("D", 0);
        CidService cidService = service(counter, savedCids);

        assertEquals("CD0000", cidService.generateCid(request()));
        assertEquals("CD0001", cidService.generateCid(request()));
        assertEquals(2, counter.getCounter());

        GeneratedCid firstSavedCid = savedCids.getFirst();
        assertEquals("CD0000", firstSavedCid.getCid());
        assertEquals("SAP", firstSavedCid.getHrSystem());
        assertEquals("DE01", firstSavedCid.getEonAccountingAreaID());
        assertEquals("12345678", firstSavedCid.getEmployeeNumber());
        assertNotNull(firstSavedCid.getCreatedAt());
    }

    @Test
    void generatesMaximumValueThenRejectsTheNextRequest() {
        CidService cidService = serviceWithCounter(CidService.MAX_COUNTER, new ArrayList<>());

        assertEquals("CDZZZZ", cidService.generateCid(request()));
        assertThrows(CidRangeExhaustedException.class, () -> cidService.generateCid(request()));
    }

    private CidService serviceWithCounter(long counter, List<GeneratedCid> savedCids) {
        return service(new CidCounter("D", counter), savedCids);
    }

    private CidService service(CidCounter counter, List<GeneratedCid> savedCids) {
        return new CidService(TestRepositories.counterRepository(counter),
                TestRepositories.generatedCidRepository(savedCids), "D");
    }

    private CidRequest request() {
        CidRequest request = new CidRequest();
        request.setHrSystem("SAP");
        request.setEonAccountingAreaID("DE01");
        request.setEmployeeNumber("12345678");
        return request;
    }
}
