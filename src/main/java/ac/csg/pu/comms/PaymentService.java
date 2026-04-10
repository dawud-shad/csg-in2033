package ac.csg.pu.comms;

import ac.csg.pu.comms.model.Payment;
import ac.csg.pu.comms.model.Response;

import java.util.ResourceBundle;

public class PaymentService {
    public static Response process(Payment request) {
        Response response = new Response();
        response.status = 200;
        response.message = "Payment processing not implemented.";
        return response;
    }
}