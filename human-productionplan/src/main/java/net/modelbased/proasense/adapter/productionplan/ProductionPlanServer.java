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
package net.modelbased.proasense.adapter.productionplan;

import net.modelbased.proasense.adapter.human.AbstractHumanServer2;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/ProductionPlanServer")
public class ProductionPlanServer extends AbstractHumanServer2 {
    public final static Logger logger = Logger.getLogger(ProductionPlanServer.class);

    private HashMap<String,String> library = new HashMap<String,String>();
    private int flag = 0;


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public void readyValuesForSimpleEvents(String text){

        String trimText = text.substring(0, text.length()-1);
        String[] simpleEventValues = trimText.split("&");

        for(int i = 0; i < simpleEventValues.length; i++){
            convertToSimpleEvents(simpleEventValues[i]);
        }
    }

    public void convertToSimpleEvents(String text) {

        System.out.println(text);


        if(flag == 0){
            createLibrary();
            flag = 1;
        }

//        logger.debug(text);
        String time_value[] = {"6:00", "14:00", "22:00", "6:00"};
        String allValues[] = text.split(",");

        String formDate = allValues[0];
        String machineId = trimName(allValues[1]);
        long timestamp = System.currentTimeMillis();
        String shiftid = allValues[2];
        String productId = allValues[3];
        String shiftStartTime = time_value[(Integer.parseInt(shiftid) - 1) % 3];
        String shiftEndTime = time_value[Integer.parseInt(shiftid) % 3];

//        logger.debug(formDate + " " + shiftStartTime);

        long startMilliSec = convertToLong(formDate + " " + shiftStartTime, shiftid);
        long endMilliSec = convertToLong(formDate + " " + shiftEndTime, shiftid);

        if (allValues.length == 4) {
            makeAndPublishEvents(machineId, timestamp, shiftid, productId, startMilliSec, endMilliSec);
        } else if (allValues.length == 5) {
           // LOGGER.debug("lengden er 5");
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

        simpleEvent.setSensorId("machinePlan");
        simpleEvent.setTimestamp(timestamp);

        complexValue.setValue(machineId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("machineId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(shiftid);
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("shifId", complexValue);

        String[] libraryRow = (library.get(productId)).split(",");

        complexValue = new ComplexValue();
        complexValue.setValue(libraryRow[0]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("mouldId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(libraryRow[1]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("productId1", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(libraryRow[2]);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("productId2", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(Long.toString(prodStartTime));
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productStartTime", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(Long.toString(prodEndTime));
        complexValue.setType(VariableType.LONG);
        simpleEvent.putToEventProperties("productEndTime", complexValue);

      //  this.outputPort.publishSimpleEvent(simpleEvent);
        System.out.println(simpleEvent.toString());
        logger.debug("SimpleEvent = " + simpleEvent.toString());
    }

    private long convertToLong(String date, String id) {

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


    private String addZero(String input) {
        if (input.length() == 1) return "0" + input;
        return input;
    }


    private String trimName(String machineId){
        String newID[];
        newID = machineId.split(" ");
        String newID2[] = newID[2].split("/");
        return newID2[0];
    }


    private void createLibrary(){
        String libValues =  "KSP155.031-00U010	BMW - 155.031/032-00	155.031-00	155.032-00 ," +
                       "KSP156.013-00U010	Clio erna - 156.013/014-00	156.013-00	156.014-00 ," +
                       "KSP156.013-01U010	Clio temno siva - 156.013/014-01	156.013-01	156.014-01 ," +
                       "KSP156.013-02U010	Clio svetlo siva - 156.013/014-02	156.013-02	156.014-02 ," +
                       "KSP160.201-00U011	Astra 3300 (stara) 011 - 160.201/202-00	160.201-00	160.202-00 ," +
                       "KSP160.201-00U012	Astra 3300 (stara) 012 - 160.201/202-00	160.201-00	160.202-00 ," +
                       "KSP161.493-00U010	BMW - 161.493/494-00	161.493-00	161.494-00 ," +
                       "KSP162.979-00U010	Picasso PSA 010 - 162.979/980-00	162.979-00	162.980-00 ," +
                       "KSP162.979-00U011	Picasso PSA 011 - 162.979/980-00	162.979-00	162.980-00 ," +
                       "KSP166.071-01U010	Daimler CLK BR207 - 166.071-01/02	166.071-01	166.071-02 ," +
                       "KSP166.413-00U010	Daimler E-clase BR212 - 166.071-01/02	166.413-00	166.414-00 ," +
                       "KSP168.857-01U010	Insignia (stara) 010 - 168.857-01/02	168.857-01	168.857-02 ," +
                       "KSP168.857-01U011	Insignia (stara) 011 - 168.857-01/02	168.857-01	168.857-02 ," +
                       "KSP170.090-01U010	Pathfinder - 170.090-01/02	170.090-01	170.090-02 ," +
                       "KSP171.572-01U010	Delta (nova) - 010 171.572-01/02	171.572-01	171.572-02 ," +
                       "KSP171.572-01U011	Delta (nova) - 011 171.572-01/02	171.572-01	171.572-02 ," +
                       "KSP171.572-01U012	Delta (nova) - 012 171.572-01/02	171.572-01	171.572-02 ," +
                       "KSP172.306-01U010	Insignia GMX 350 172.306-01/02	172.306-01	172.306-02 ," +
                       "KSP173.420-01U010	Golf AFS - 173.420-01/02	173.420-01	173.420-02 ," +
                       "KSP180.585-01U010	Renault X10 - 180.585-01/02	180.585-01	180.585-02 ," +
                       "KSP187.552-01U010	Insignia (nova) - 187.552-01/02	187.552-01	187.552-02 ," +
                       "KSP187.829-01U010	Nissan X12 - 187.829-01/02	187.829-01	187.829-02 ," +
                       "KSP188.709-01U010	Renault X82 - 188.709-01/02	188.709-01	188.709-02 ," +
                       "KSP190.890-01U010	Edison X07 - 190.890-01/02	190.890-01	190.890-02 ," +
                       "KSP193.216-01U010	Porsche Cayenne 2K - 193-216-01/02	193.216-01	193.216-02 ," +
                       "KSP194.856-01U010	Omega / Cadillac - 194.856-01/02	194.856-01	194.856-02 ," +
                       "KSP195.429-01U010	Corsa 010 - 195.429-01/02	195.429-01	195.429-02 ," +
                       "KSP195.429-01U011	Corsa 011 - 195.429-01/02	195.429-01	195.429-02 ," +
                       "KSP196.197-01U010	Renault HFE - 196.197-01/02	196.197-01	196.197-02 ," +
                       "KSP197.186-01U010	Jaguar - 197.186-01/02	197.186-01	197.186-02 ," +
                       "KSP201.888-01U010	Renault Scenic 2K - 201.888-01/02	201.888-01	201.888-02 ," +
                       "KSP254.253-00U010	Golf Halogen - 254.253/254-000	254.253-000	254.254-000 ," +
                       "KSP270.533-00U010	Lancia - 270.533/534-00	270.533-00	270.534-00 ," +
                       "KSP271.535-00U010	Twingo - 271.535/536-00	271.535-00	271.536-00";

        String[] splitLib = libValues.split(",");

        for(int i = 0; i < splitLib.length; i++){
            String[] splitValues = splitLib[i].split("\\t");
            library.put(splitValues[1], splitValues[0] + "," + splitValues[2] + ","+splitValues[3]);
        }
    }
}
