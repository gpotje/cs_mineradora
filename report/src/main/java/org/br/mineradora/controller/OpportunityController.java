package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.service.OpportunityService;
//import org.eclipse.microprofile.jwt.JsonWebToken;

//import javax.annotation.security.RolesAllowed;
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

//    @Inject
//    JsonWebToken token;

    @Inject
    OpportunityService opportunityService;

//    @GET
//    @Path("/data")
//   // @RolesAllowed({"user","manager"})
//    public List<OpportunityDTO> generateReport(){
//
//        return opportunityService.generateOpportunityData();
//
//    }

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    // @RolesAllowed({"user","manager"})
    public Response generateReport(){

        try{

            return Response.ok(opportunityService.generateOpportunityData(),
                    MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition",
                            "attachment; filename = "+ new Date() + "--oportunidades-venda.csv")
                    .build();


        }catch (ServerErrorException e){


            return Response.serverError().build();
        }



    }

}
