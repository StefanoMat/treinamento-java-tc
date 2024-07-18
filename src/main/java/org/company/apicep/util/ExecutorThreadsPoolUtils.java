package org.company.apicep.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ExecutorThreadsPoolUtils<V> {
    public List<V> extractResponse(List<Future<V>> futureResults) {
        List<V> response = new ArrayList<>();

        futureResults.forEach(responseFuture -> {
            try {
                if (responseFuture.isDone() && !responseFuture.isCancelled()) {
                    response.add(responseFuture.get());
                } else if (responseFuture.isCancelled()) {
                    log.info("Callable was cancelled");
                } else {
                    log.info("Some error occurred");
                    log.info("isDone {}", responseFuture.isDone());
                    log.info("isCancelled {}", responseFuture.isCancelled());
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error getting responses {}", e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected Error getting responses {}", e.getMessage());
            }
        });
        return response;
    }
}
