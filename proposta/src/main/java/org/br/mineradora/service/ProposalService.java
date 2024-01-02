package org.br.mineradora.service;

import org.br.mineradora.dto.ProposalDetailsDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ProposalService {

    ProposalDetailsDTO findFullProposal(long id);

    void createNewProposal(ProposalDetailsDTO dto);

    void removeProposal(long id);
}
