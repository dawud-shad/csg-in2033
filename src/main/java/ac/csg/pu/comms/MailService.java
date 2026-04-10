package ac.csg.pu.comms;

import ac.csg.pu.comms.model.Mail;
import ac.csg.pu.comms.model.Response;

public class MailService {
    public static Response process(Mail request) {
        Response response = new Response();
        response.status = 200;
        response.message = "Mail processing not implemented.";
        return response;
    }
}