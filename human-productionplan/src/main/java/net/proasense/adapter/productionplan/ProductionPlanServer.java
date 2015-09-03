package net.proasense.adapter.productionplan;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import javassist.bytecode.stackmap.TypeData;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by shahzad on 18.08.15.
 */

/**
 * Created by shahzad on 15.08.15.
 */

@Path("/productionPlan")
public class ProductionPlanServer extends AbstractBaseAdapter {
    // The Java method will process HTTP GET requests

    final static Logger LOGGER = LoggerFactory.getLogger(ProductionPlanServer.class);

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text) {

        LOGGER.debug(text);
        String time_value[] = {"6:00", "14:00", "22:00", "6:00"};
        String allValues[] = text.split(",");

        String formDate = allValues[0];
        String machineId = allValues[1];
        long timestamp = System.currentTimeMillis();
        String shiftid = allValues[2];
        String productId = allValues[3];
        String shiftStartTime = time_value[(Integer.parseInt(shiftid) - 1) % 3];
        String shiftEndTime = time_value[Integer.parseInt(shiftid) % 3];

        LOGGER.debug(formDate + " " + shiftStartTime);

        long startMilliSec = convertToLong(formDate + " " + shiftStartTime, shiftid);
        long endMilliSec = convertToLong(formDate + " " + shiftEndTime, shiftid);

        if (allValues.length == 4) {
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, startMilliSec, endMilliSec);
        } else if (allValues.length == 5) {
            LOGGER.debug("lengden er 5");
        } else if (allValues.length == 6) {
            shiftEndTime = allValues[5];
            long handoverTime = convertToLong(formDate+" "+shiftEndTime, shiftid);
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, startMilliSec, handoverTime);
            startMilliSec = handoverTime;
            shiftEndTime = time_value[(Integer.parseInt(shiftid)) % 3];
            endMilliSec = convertToLong(formDate+" "+shiftEndTime, shiftid);
            productId = allValues[4];
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, startMilliSec, endMilliSec);
        }
    }

    public void makeAndPublishEvents(String machineId, long timestamp, String shiftid,
                                     String productId, long prodStartTime, long prodEndTime) {

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
        complexValue.setValue(Long.toString(prodStartTime));
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productStartTime", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(Long.toString(prodEndTime));
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productEndTime", complexValue);

        this.outputPort.publishSimpleEvent(simpleEvent);
        LOGGER.debug(simpleEvent.toString());
        System.out.println(simpleEvent.toString());
    }

    long convertToLong(String date, String id) {

        String splitDateTime[] = date.split(" ");
        String splitDate[] = splitDateTime[0].split("/");
        String convertedDate = addZero(splitDate[0])+"/"+addZero(splitDate[1])+"/"+splitDate[2]+" "+splitDateTime[1];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d = null;
        try {
            d = simpleDateFormat.parse(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateToMilli = d.getTime();

        if (id.equals("4")) {

           dateToMilli += 86400000;
        } else {

        }

        return dateToMilli;
    }

    public String addZero(String input) {
        if (input.length() == 1) return "0" + input;
        return input;
    }
}
