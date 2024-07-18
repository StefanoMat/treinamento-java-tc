package org.company.apicep.orchestrator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.apicep.callable.AddressCallable;
import org.company.apicep.model.Address;
import org.company.apicep.service.StepAddressService;
import org.company.apicep.util.ExecutorThreadsPoolUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class AddressOrchestrator {
    private final StepAddressService stepAddressService;
    private final ExecutorThreadsPoolUtils<Address> executorThreadsPoolUtils;
    public List<Address> createAddresses(List<Address> addresses) {
        try {
            log.info("Executing threads");
            return executeThreads(addresses);
        } catch (Exception e) {
            return null;
        }
    }

    @Async
    public CompletableFuture<Boolean> createAddressesAsync(List<Address> addresses) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Executing threads");
                executeThreads(addresses);
                return true;
            } catch (Exception e) {
                log.error("Error while creating addresses", e);
                return false;
            }
        });
    }

    private List<Address> executeThreads(List<Address> addresses) throws InterruptedException {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<AddressCallable> callables = wrapAddressOnCallable(addresses);
        List<Future<Address>> bulkInsertsResults = executorService.invokeAll(callables);
        executorService.shutdown();
        return executorThreadsPoolUtils.extractResponse(bulkInsertsResults);
    }

    private List<AddressCallable> wrapAddressOnCallable(List<Address> addresses) {
        return addresses.parallelStream().map(address -> new AddressCallable(address, stepAddressService)).collect(Collectors.toList());
    }
}
