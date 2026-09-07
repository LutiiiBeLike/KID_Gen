package de.eon.cidgen.support;

import de.eon.cidgen.entity.CidCounter;
import de.eon.cidgen.entity.GeneratedCid;
import de.eon.cidgen.repository.CidCounterRepository;
import de.eon.cidgen.repository.GeneratedCidRepository;

import java.lang.reflect.Proxy;
import java.util.List;

/** Small in-memory repository doubles for service and controller tests. */
public final class TestRepositories {

    private TestRepositories() {
    }

    public static CidCounterRepository counterRepository(CidCounter counter) {
        return (CidCounterRepository) Proxy.newProxyInstance(
                TestRepositories.class.getClassLoader(),
                new Class<?>[]{CidCounterRepository.class},
                (proxy, method, arguments) -> {
                    if (method.getName().equals("findByRangeLetterForUpdate")) {
                        return counter;
                    }
                    if (method.getName().equals("save")) {
                        return arguments[0];
                    }
                    return null;
                });
    }

    public static GeneratedCidRepository generatedCidRepository(List<GeneratedCid> savedCids) {
        return (GeneratedCidRepository) Proxy.newProxyInstance(
                TestRepositories.class.getClassLoader(),
                new Class<?>[]{GeneratedCidRepository.class},
                (proxy, method, arguments) -> {
                    if (method.getName().equals("save")) {
                        GeneratedCid generatedCid = (GeneratedCid) arguments[0];
                        savedCids.add(generatedCid);
                        return generatedCid;
                    }
                    return null;
                });
    }
}
