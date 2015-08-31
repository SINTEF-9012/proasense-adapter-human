package net.proasense.adapter.productionplan;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shahzad on 18.08.15.
 */

/**
 * Created by shahzad on 15.08.15.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/productionPlan")
public class ProductionPlanServer {
    // The Java method will process HTTP GET requests

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text){

        String allValues[] = text.split(",");

        String sensorId = allValues[0];
        long timestamp = System.currentTimeMillis();
        int shiftid_value = 4;
        String productId = allValues[1];

        System.out.println(text);

        ComplexValue complexValue = new ComplexValue();
         Map<String, ComplexValue> eventProperties = new HashMap<String, ComplexValue>();
        SimpleEvent simpleEvent = new SimpleEvent();

        sensorId = "materialchange"; //
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


       // this.outputPort.publishSimpleEvent(simpleEvent);

    }
}
