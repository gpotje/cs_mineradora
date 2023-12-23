package org.br.mionera.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.br.mionera.client.CurrencyPriceClient;
import org.br.mionera.dto.CurrencyPriceDTO;
import org.br.mionera.dto.QuotationDTO;
import org.br.mionera.entity.QuotationEntity;
import org.br.mionera.message.KafkaEvents;
import org.br.mionera.repository.QuotationRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuotationService {
    
    @Inject
    @RestClient//busca em uma API externa
    CurrencyPriceClient cpc;

    @Inject
    QuotationRepository qr;

    @Inject
    KafkaEvents ke;


    public void getCurrencyPriceInfo(){

        CurrencyPriceDTO cpDTO = cpc.getPriceByPair("USD-BRL");

        if(updateCurrentInforPrice(cpDTO)){
            ke.sendNewKafkaEvent(
                QuotationDTO.builder()
                .currencyPrice(new BigDecimal(cpDTO.getUSDBRL().getBid()))
                .build());
        
        }
    }

    private boolean updateCurrentInforPrice(CurrencyPriceDTO dto){

        BigDecimal cp = new BigDecimal(dto.getUSDBRL().getBid());
        boolean up = false;

        List<QuotationEntity> list = qr.findAll().list();

        if (list.isEmpty()){

            saveQuotation(dto);
            up = true;

        }else{

            QuotationEntity lastDollarPrice = list.get(list.size()-1);

            if(cp.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()){
                 up = true;
                 saveQuotation(dto);
            }

        }
        
        return up;
    }

    private void saveQuotation(CurrencyPriceDTO dto){
        QuotationEntity quotationEntity = new QuotationEntity();
        quotationEntity.setDate(new Date(0));
        quotationEntity.setCurrencyPrice(new BigDecimal(dto.getUSDBRL().getBid()));
        quotationEntity.setPctChange(dto.getUSDBRL().getPctChange());
        quotationEntity.setPair("USD-BRL");

        qr.persist(quotationEntity);
    }

}
