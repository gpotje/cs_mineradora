package org.br.mionera.scheduler;

import org.br.mionera.service.QuotationService;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class QuotationScheduler {

    @Inject
    QuotationService qs; 

    @Transactional
    @Scheduled(every = "35s", identity = "task-job")
    void schedule(){
        qs.getCurrencyPriceInfo();
    }
    
}
