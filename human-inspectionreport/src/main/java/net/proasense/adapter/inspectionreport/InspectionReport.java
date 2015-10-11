package net.proasense.adapter.inspectionreport;


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

@Path("/InspectionReportForm")
public class InspectionReport extends AbstractBaseAdapter {

    Logger logger = Logger.getLogger("net.proasense.adapter.inspectionreport.InspectionReport");
    String sensor_id = adapterProperties.getProperty("proasense.adapter.base.sensorid");

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text) throws ParseException {
        SimpleEvent simpleEvent = null;
        String[] splitOnUnderline = text.split("_");

        String[] objecInformation = splitOnUnderline[0].split(",");
        String[] levelOneInfo = new String[7];

        for(int i = 0; i < 7; i++){
            levelOneInfo[i] = objecInformation[i];
        }

        simpleEvent = makeLevelOneEvent(levelOneInfo);

        String lastWord = objecInformation[objecInformation.length-1];
            logger.debug(lastWord);

        //stoppet her.
        if(lastWord.equals("Visual inspection")){
            visualInspection(simpleEvent, splitOnUnderline[1]);
        }else if(lastWord.equals("Vibration monitoring")){
            String[] mappedArray = splitOnUnderline[1].split(",");
            String[] mappedArrayVal = splitOnUnderline[2].split(",");
            String[] finalArray = new String[8];

            for(int i = 4; i < 8; i++){
                finalArray[i-4] = mappedArray[i];
                finalArray[i] = mappedArrayVal[i-4];
            }

            vibrationMonitoring(simpleEvent, finalArray);
        }else if(lastWord.equals("Oil sampling")){

            String[] mappedArray = splitOnUnderline[1].split(",");
            String[] mappedArrayVal = splitOnUnderline[2].split(",");
            String[] finalArray = new String[8];

            for(int i = 8; i < 12; i++){
                finalArray[i-8] = mappedArray[i];
                finalArray[i-4] = mappedArrayVal[i-4];
            }
            oilSampling(simpleEvent, finalArray);
        }

        publishEvent(simpleEvent);
    }

    public SimpleEvent visualInspection(SimpleEvent simpleEvent, String visualInspectionVal){
        logger.debug("traversing visualInspection method");
        String[] visualInspectionValues = visualInspectionVal.split(",");

        simpleEvent = createEvent(simpleEvent, "swivelCriticalityRating", visualInspectionValues[0], VariableType.LONG);
        simpleEvent = createEvent(simpleEvent, "swivelCriticalityRatingAcceptance", visualInspectionValues[1], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "gearboxCriticalityRating", visualInspectionValues[2], VariableType.LONG);
        simpleEvent = createEvent(simpleEvent, "gearboxCriticalityRatingAcceptance", visualInspectionValues[3], VariableType.STRING);
        return simpleEvent;
    }

    public SimpleEvent vibrationMonitoring(SimpleEvent simpleEvent, String[] mapping){
        logger.debug("trversing vibrationMonitoring method");
        simpleEvent = createEvent(simpleEvent, "swivelVibrationVelocity", mapping[0], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "swivelVibrationAcceleration", mapping[1], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "gearboxVibrationVelocity", mapping[2], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "gearboxVibrationAcceleration", mapping[3], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "swivelVibrationVelocityAcceptance", mapping[4], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "swivelVibrationAccelerationAcceptance", mapping[5], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "gearboxVibrationVelocityAcceptance", mapping[6], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "gearboxVibrationAccelerationAcceptance", mapping[7], VariableType.STRING);

    return simpleEvent;
    }

    public SimpleEvent oilSampling(SimpleEvent simpleEvent, String[] mapping){
        logger.debug("in oilSampling method");

        simpleEvent = createEvent(simpleEvent, "swivelOilSamplingFeContent", mapping[0], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "swivelOilSamplingViscosity", mapping[1], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "gearboxOilSamplingFeContent", mapping[2], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "gearboxOilSamplingViscocity", mapping[3], VariableType.DOUBLE);
        simpleEvent = createEvent(simpleEvent, "swivelOilSamplingFeContentAcceptance", mapping[4], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "swivelOilSamplingViscosityAcceptance", mapping[5], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "gearboxOilSamplingFeContentAcceptance", mapping[6], VariableType.STRING);
        simpleEvent = createEvent(simpleEvent, "gearboxOilSamplingViscocityAcceptance", mapping[7], VariableType.STRING);

        return simpleEvent;
    }

    public SimpleEvent makeLevelOneEvent(String[] basicInfo) throws ParseException {
        SimpleEvent simpleEvent = new SimpleEvent();

        //setting sensor ID
        simpleEvent.setSensorId(sensor_id);

        //convert date and time to long.
        String dateTime = basicInfo[1]+" "+basicInfo[2];
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        Date parsedDate = formatter.parse(dateTime);
        long longTime = parsedDate.getTime();

        //setting long value of date.
        simpleEvent.setTimestamp(longTime);
        logger.debug("long is " + longTime + " date is " + dateTime);

        ComplexValue complexValue = new ComplexValue();
        complexValue.setValue(basicInfo[0]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("rigName", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(basicInfo[3]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("responsiblePerson", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(basicInfo[4]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("productCode", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(basicInfo[5]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("equipmentType", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(basicInfo[6]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("inspectionType", complexValue);

        logger.debug(simpleEvent.toString());
        return simpleEvent;
    }

    public void publishEvent(SimpleEvent simpleEvent){
        outputPort.publishSimpleEvent(simpleEvent);
        logger.debug(simpleEvent.toString());
    }

    public SimpleEvent createEvent(SimpleEvent simpleEvent, String eventName,
                            String value, VariableType variableType){

        ComplexValue complexValue = new ComplexValue();
        complexValue.setType(variableType);
        complexValue.setValue(value);
        simpleEvent.putToEventProperties(eventName, complexValue);
        return simpleEvent;
    }
}
