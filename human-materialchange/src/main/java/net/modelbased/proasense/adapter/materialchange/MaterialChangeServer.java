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
package net.modelbased.proasense.adapter.materialchange;

import net.modelbased.proasense.adapter.human.AbstractHumanServer;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;

import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/MaterialChangeServer")
public class MaterialChangeServer extends AbstractHumanServer {
    public final static Logger logger = Logger.getLogger(MaterialChangeServer.class);

    private String sensorId = adapterProperties.getProperty("proasense.adapter.base.sensorid");


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response convertToSimpleEvents(String text){
        String allValues[] = text.split(",");

        ComplexValue complexValue = new ComplexValue();
        SimpleEvent simpleEvent = new SimpleEvent();

        long timeStamp = System.currentTimeMillis();

        String machineId = allValues[0];
        String materialId =allValues[1];

        simpleEvent.sensorId = sensorId;
        simpleEvent.timestamp = timeStamp;

        complexValue.setValue(machineId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("machineId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(materialId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("materialId", complexValue);

        this.outputPort.publishSimpleEvent(simpleEvent);
        logger.debug("SimpleEvent = " + simpleEvent.toString());

        return Response.status(201).entity(text).build();
    }
}
