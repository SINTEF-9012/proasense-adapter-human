package net.proasense.adapter.materialcertificate;

import eu.proasense.internal.ComplexValue;
import eu.proasense.internal.SimpleEvent;
import eu.proasense.internal.VariableType;
import net.modelbased.proasense.adapter.AbstractHumanAdapterServer;
import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by shahzad on 15.08.15.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/certificateForm")
//public class MaterialCertificateServer extends AbstractBaseAdapter {
public class MaterialCertificateServer extends AbstractHumanAdapterServer {
    // The Java method will process HTTP GET requests

    protected String sensorId = adapterProperties.getProperty("proasense.adapter.base.sensorid");
    final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MaterialCertificateServer.class);

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void convertToSimpleEvents(String text){

        System.out.println(text);
        String allValues[] = text.split(",");

        ComplexValue complexValue = new ComplexValue();
       // Map<String, ComplexValue> eventProperties = new HashMap<String, ComplexValue>();
        SimpleEvent simpleEvent = new SimpleEvent();

        //String sensorId = "materialcertificate"; // les denne fra propertiesfilen
        long timeStamp = System.currentTimeMillis(); //
        String orderNumber =allValues[1]; //
        String materialId = allValues[0]; //
        String mvrMethod =  allValues[2]; //
        String mvrAverage = allValues[3]; //
        String mvrMin = allValues[4];
        String mvrMax = allValues[5];
     //   String printOut = allValues[6];

        simpleEvent.sensorId = sensorId;
        simpleEvent.timestamp = timeStamp;

        complexValue.setValue(orderNumber);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("orderNumber", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(materialId);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("materialId", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(mvrMethod);
        complexValue.setType(VariableType.STRING);
        simpleEvent.putToEventProperties("mvrMethod", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(mvrAverage);
        complexValue.setType(VariableType.DOUBLE);
        simpleEvent.putToEventProperties("mvrAverage", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(mvrMin);
        complexValue.setType(VariableType.DOUBLE);
        simpleEvent.putToEventProperties("mvrMin", complexValue);

        complexValue = new ComplexValue();
        complexValue.setValue(mvrMax);
        complexValue.setType(VariableType.DOUBLE);
        simpleEvent.putToEventProperties("mvrMax", complexValue);


        this.outputPort.publishSimpleEvent(simpleEvent);
        System.out.println("DEBUG AFTER " + simpleEvent.toString());

    }
}
