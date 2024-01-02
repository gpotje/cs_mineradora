package org.br.mineradora.service;

import org.br.mineradora.controller.ProposalController;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;


@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    private final Logger LOG = LoggerFactory.getLogger(ProposalServiceImpl.class);

    @Inject
    ProposalRepository repository;
    @Inject
    KafkaEvent event;

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {

        ProposalEntity proposal = repository.findById(id);

        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .country(proposal.getCountry())
                .priceTonne(proposal.getPriceTonne())
                .customer(proposal.getCustomer())
                .tonnes(proposal.getTonnes())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO dto) {

        ProposalDTO proposal = buildAndSaveNewProposal(dto);
        LOG.info("---antes kafka ---");
       event.sendNewKafkaEvent(proposal);
        LOG.info("---depois kafka ---");

    }

    @Transactional
    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO dto) {

        try {
            ProposalEntity proposal = new ProposalEntity();

            proposal.setCreated(new Date());
            proposal.setProposalValidityDays(dto.getProposalValidityDays());
            proposal.setCountry(dto.getCountry());
            proposal.setCustomer(dto.getCustomer());
            proposal.setPriceTonne(dto.getPriceTonne());
            proposal.setTonnes(dto.getTonnes());
            repository.persist(proposal);
           LOG.info("---persist proposal with success ---");

            return ProposalDTO.builder()
                    .proposalId(repository.findByCustomer(proposal.getCustomer()).get().getId())
                    .priceTonne(proposal.getPriceTonne())
                    .customer(proposal.getCustomer())
                    .build();

        } catch (Exception e) {
            LOG.info("---persist proposal with error --- " + e.getLocalizedMessage());
             e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        repository.deleteById(id);
    }
}
