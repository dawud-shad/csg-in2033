package ac.csg.pu.comms;

import ac.csg.pu.comms.model.Mail;
import ac.csg.pu.comms.model.Payment;
import ac.csg.pu.comms.model.Response;
import io.javalin.Javalin;

public class RestServer {

    private static Javalin app;

    public static void start(int port) {
        app = Javalin.create().start(port);

        // Payment endpoint
        app.post("/pay", ctx -> {
            Payment request = ctx.bodyAsClass(Payment.class);
            Response response = PaymentService.process(request);
            ctx.json(response);
        });

        // Mail endpoint
        app.post("/mail", ctx -> {
            Mail request = ctx.bodyAsClass(Mail.class);
            Response response = MailService.process(request);
           ctx.json(response);
        });
    }

    public static void stop() {
        if (app != null) app.stop();
    }
}