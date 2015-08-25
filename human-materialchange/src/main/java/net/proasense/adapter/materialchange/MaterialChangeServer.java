package net.proasense.adapter.materialchange;

/**
 * Created by shahzad on 18.08.15.
 */

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by shahzad on 15.08.15.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/changeForm")
public class MaterialChangeServer extends AbstractBaseAdapter {
    // The Java method will process HTTP GET requests

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response convertToSimpleEvents(String text){

        String allValues[] = text.split(",");

        System.out.println("allValues[0] er "+allValues[0]+" allValues[1] er "+allValues[1]);
        ComplexValue complexValue = new ComplexValue();
        // Map<String, ComplexValue> eventProperties = new HashMap<String, ComplexValue>();
        SimpleEvent simpleEvent = new SimpleEvent();

        String sensorId = "materialchange"; //
        long timeStamp = System.currentTimeMillis(); //

        String machineId = allValues[0]; //
        String materialId =allValues[1]; //


        simpleEvent.sensorId = sensorId;
        simpleEvent.timestamp = timeStamp;

        complexValue.setValue(machineId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("machineid", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(materialId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("materialId", complexValue);

        System.out.println("før sending.");
        this.outputPort.publishSimpleEvent(simpleEvent);
        System.out.println("etter sending.");

        return Response.status(201).entity(text).build();
    }
}
