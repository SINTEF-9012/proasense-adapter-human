/**
 * Copyright (C) 2014-2015 SINTEF
 *
 *     Brian Elvesæter <brian.elvesater@sintef.no>
 *     Shahzad Karamat <shazad.karamat@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.modelbased.proasense.adapter.maintenancereport;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.human.AbstractHumanServer;
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

@Path("/MaintenanceReportServer")
public class MaintenanceReportServer extends AbstractHumanServer {
    Logger logger = Logger.getLogger(MaintenanceReportServer.class);

    String sensor_id = adapterProperties.getProperty("proasense.adapter.base.sensorid");


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text) throws ParseException {
        String[] separateValues = text.split(",");

        String rigName = separateValues[0];
        String date = separateValues[1];
        String time = separateValues[2];
        String dateAndTime = date+" "+time;
        DateFormat formatter = new SimpleDateFormat("dd-mm-yyyy HH:mm");

        Date parsedDate = formatter.parse(dateAndTime);
        long longTime = parsedDate.getTime();
        logger.debug("dateTimer er " + dateAndTime + " long er " + longTime);

        String maintenanceResponsiblePerson = separateValues[3];
        String equipmentType = separateValues[4];
        String productCode = separateValues[5];
        String maintenancePerformed = separateValues[6];
        String timeUsage = separateValues[7];
        String criticalValue = separateValues[8];
        String sustainementCost = separateValues[9];
        String equipmentCondition = separateValues[10];
        String maintenanceAccepted = separateValues[11];

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
        simpleEvent.putToEventProperties("maintenanceDuration", complexValue);

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

        this.outputPort.publishSimpleEvent(simpleEvent);
        logger.debug(simpleEvent.toString());
    }
}
