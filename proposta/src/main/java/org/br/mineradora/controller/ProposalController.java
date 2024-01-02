package org.br.mineradora.controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
public class ProposalController {

    @Inject
    ProposalService service;

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @GET
    @Path("/{id}")
   // @RolesAllowed({"user","manager"})
    public Response findDetailsProposal(@PathParam("id") long id){
        return Response.ok(service.findFullProposal(id)).build();
     }

    @POST
    //@RolesAllowed("proposal-customer")
    public Response createProposal(ProposalDetailsDTO proposalDetails){

        LOG.info("--- Recebendo Proposta de Compra ---");

        try {

            service.createNewProposal(proposalDetails);
            LOG.info("---voltou para o controller ---");
            return Response.ok().build();

        } catch (Exception e) {

            return Response.serverError().build();

        }

    }


    @DELETE
    @Path("/{id}")
  //  @RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") long id){
         try {
            service.removeProposal(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }


}
