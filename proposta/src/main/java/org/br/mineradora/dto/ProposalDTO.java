package org.br.mineradora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import java.math.BigDecimal;


@Jacksonized
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalDTO {

    private Long proposalId;

    private String customer;

    private BigDecimal priceTonne;

}
