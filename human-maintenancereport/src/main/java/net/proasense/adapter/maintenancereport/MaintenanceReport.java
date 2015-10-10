package net.proasense.adapter.maintenancereport;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shahzad on 01.10.2015.
 */
@Path("/MaintenanceReport")
public class MaintenanceReport extends AbstractBaseAdapter{
    Logger logger = Logger.getLogger("net.proasense.adapter.maintenancereport.MaintenanceReport");
    String sensor_id = adapterProperties.getProperty("proasense.adapter.base.sensorid");

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text) throws ParseException {

        String[] seperateValues = text.split(",");

        String rigName = seperateValues[0];
        String date = seperateValues[1];
        String time = seperateValues[2];
        String dateAndTime = date+" "+time;
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm");

        Date parsedDate = formatter.parse(dateAndTime);
        long longTime = parsedDate.getTime();
        logger.debug("dateTimer er " + dateAndTime + " long er " + longTime);

        String maintenanceResponsiblePerson = seperateValues[3];
        String equipmentType = seperateValues[4];
        String productCode = seperateValues[5];
        String maintenancePerformed = seperateValues[6];
        String timeUsage = seperateValues[7];
        String criticalValue = seperateValues[8];
        String sustainementCost = seperateValues[9];
        String equipmentCondition = seperateValues[10];
        String maintenanceAccepted = seperateValues[11];

        SimpleEvent simpleEvent = new SimpleEvent();
        simpleEvent.setTimestamp(longTime);
        simpleEvent.setSensorId(sensor_id);

        ComplexValue complexValue = new ComplexValue();
        complexValue.setValue(rigName);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("rigName", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(maintenanceResponsiblePerson);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("responsiblePerson", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(equipmentType);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("equipmentType", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(productCode);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("productCode", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(maintenancePerformed);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("maintenanceType", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(timeUsage);
        complexValue.setType(VariableType.DOUBLE);
        simpleEvent.putToEventProperties("miantenanceDuration", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(criticalValue);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("findingCriticality", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(sustainementCost);
        complexValue.setType(VariableType.DOUBLE);
        simpleEvent.putToEventProperties("maintenanceCost", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(equipmentCondition);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("equipmentCondition", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(maintenanceAccepted);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("equipmentConditionAcceptance", complexValue);

        logger.debug(simpleEvent.toString());

    }
}
