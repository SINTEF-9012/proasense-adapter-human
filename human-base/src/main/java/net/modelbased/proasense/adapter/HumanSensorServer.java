/**
 * Copyright (C) 2014-2015 SINTEF
 *
 *     Brian Elves�ter <brian.elvesater@sintef.no>
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
package net.modelbased.proasense.adapter;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// test GIT
@Path("/humansensorserver")
public class HumanSensorServer extends AbstractBaseAdapter {

	ArrayList al = new ArrayList();


	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response HumanSensorServer(String text){

		System.out.println(text);

//		String zooKeeper = "89.216.116.44:2181";
//		String topic = "proasense.datainfrastructure.mhwirth.all";

		// Create the Kakfa producer
//		producer = createProducer(zooKeeper);

		try {
			// Convert to simple event
			SimpleEvent event = convertToSimpleEvent(text);
			System.out.println("SimpleEvent: " + event.toString());

			// Serialize message
//			TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
//			byte[] bytes = serializer.serialize(event);

			// Publish message
//			ProducerRecord<String, byte[]> message = new ProducerRecord<String, byte[]>(topic, bytes);
//			producer.send(message);
			this.outputPort.publishSimpleEvent(event);
		}
//		catch (TException ignore) {
//
//		}
		finally {
//			producer.close();
			this.outputPort.close();
		}

		return Response.status(201).entity(text).build();
	}


	private SimpleEvent convertToSimpleEvent(String text){
		String[] splitText = text.split("-");
		String[] sensorValues = splitText[0].split(",");
		String[] valueTypes = splitText[1].split(",");
		String sensorId = valueTypes[0];

		// Process event properties
		Map<String, ComplexValue> properties = new HashMap<String, ComplexValue>();

		String processedValues = null;
		int n = 1;
		for (int i = 0; i < sensorValues.length; i++) {
			i++;

            String propertyKey = sensorValues[i++];
            String propertyValue = sensorValues[i];
            String propertyType = valueTypes[n++];

            // Define complex value
			ComplexValue value = new ComplexValue();
			value.setValue(propertyValue);

            if (propertyType.matches("LONG")) {
                value.setType(VariableType.LONG);
            }
            if (propertyType.matches("STRING")) {
                value.setType(VariableType.STRING);
            }
            if (propertyType.matches("DOUBLE")) {
                value.setType(VariableType.DOUBLE);
            }
            if (propertyType.matches("BLOB")) {
                value.setType(VariableType.BLOB);
            }

			properties.put(propertyKey, value);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		String sensorTime = dateFormat.format(date);

		SimpleEvent event = new SimpleEvent();
        event.setSensorId(sensorId);
        event.setEventProperties(properties);

		return event;
	}
}
