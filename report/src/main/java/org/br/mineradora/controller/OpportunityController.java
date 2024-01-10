package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.service.OpportunityService;
import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.jwt.JsonWebToken;

//import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Date;
import java.util.List;

@Path("/api/opportunity")
@Authenticated
public class OpportunityController {

    @Inject
    JsonWebToken token;

    @Inject
    OpportunityService opportunityService;

    @GET
    @Path("/data")
    @RolesAllowed({"user","manager"})
    public List<OpportunityDTO> generateReport(){

        return opportunityService.generateOpportunityData();

    }



}
