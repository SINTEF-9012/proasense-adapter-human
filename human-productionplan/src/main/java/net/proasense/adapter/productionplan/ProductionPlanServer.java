package net.proasense.adapter.productionplan;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;

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
public class ProductionPlanServer  extends AbstractBaseAdapter{
    // The Java method will process HTTP GET requests

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text) {


        String time_value[] = {"6:00", "14:00", "22:00", "6:00"};
        String allValues[] = text.split(",");


        String machineId = allValues[0];
        long timestamp = System.currentTimeMillis();
        String shiftid = allValues[1];
        String productId = allValues[2];
        String shiftStartTime = time_value[(Integer.parseInt(shiftid)-1)%3];
        String shiftEndTime = time_value[Integer.parseInt(shiftid)%3];

        if (allValues.length == 3) {
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, shiftStartTime, shiftEndTime);
        } else if (allValues.length == 5) {
            shiftEndTime = allValues[4];
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, shiftStartTime, shiftEndTime);
            shiftStartTime = shiftEndTime;
            shiftEndTime = time_value[(Integer.parseInt(shiftid))%3];
            productId = allValues[3];
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, shiftStartTime, shiftEndTime);
        }
    }

    public void makeAndPublishEvents(String machineId, long timestamp, String shiftid,
                                     String productId, String prodStartTime, String prodEndTime){

         ComplexValue complexValue = new ComplexValue();
         Map<String, ComplexValue> eventProperties = new HashMap<String, ComplexValue>();
         SimpleEvent simpleEvent = new SimpleEvent();

        simpleEvent.setSensorId(machineId);
        simpleEvent.setTimestamp(timestamp);

        complexValue.setValue(shiftid);
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("shifId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(productId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("productId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(prodStartTime);
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productStartTime", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(prodEndTime);
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productEndTime", complexValue);


        this.outputPort.publishSimpleEvent(simpleEvent);

    }
}
