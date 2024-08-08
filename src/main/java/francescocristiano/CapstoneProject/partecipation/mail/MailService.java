package francescocristiano.CapstoneProject.partecipation.mail;


import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${mailjet.apikey}")
    private String apiKey;

    @Value("${mailjet.apisecret}")
    private String apiSecret;

    public void sendInvitationEmail(String email, String firstName, String lastName, String acceptUrl, String rejectUrl) {
        MailjetClient client = new MailjetClient(apiKey, apiSecret, new ClientOptions("v3.1"));

        String imageUrl = "https://res.cloudinary.com/dqquinecs/image/upload/v1723048300/AthleteX_-_colore_5_osdcsg.png";

        String emailBody = "<div style=\"font-family: Arial, sans-serif; color: #333;\">"
                + "<h2 style=\"color: #1a73e8;\">Ciao " + firstName + " " + lastName + "!</h2>"
                + "<p>Sei stato invitato ad unirti al nostro team. Siamo entusiasti di averti con noi!</p>"
                + "<table style=\"width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px;\">"
                + "  <tr>"
                + "    <td style=\"text-align: center;\">"
                + "      <img src=\"" + imageUrl + "\" alt=\"Team Banner\" style=\"width: 100%; max-width: 600px; height: auto; border-radius: 10px;\" />"
                + "    </td>"
                + "  </tr>"
                + "  <tr>"
                + "    <td style=\"padding: 20px; text-align: center;\">"
                + "      <a href=\"" + acceptUrl + "\" style=\"display: inline-block; padding: 15px 25px; margin-right: 10px; color: #ffffff; background-color: #28a745; text-decoration: none; border-radius: 5px;\">Accetta l'invito</a>"
                + "      <a href=\"" + rejectUrl + "\" style=\"display: inline-block; padding: 15px 25px; color: #ffffff; background-color: #dc3545; text-decoration: none; border-radius: 5px;\">Rifiuta l'invito</a>"
                + "    </td>"
                + "  </tr>"
                + "</table>"
                + "<p>Grazie,<br/>Il Team</p>"
                + "</div>";

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "frankrys97@gmail.com")
                                        .put("Name", "Francesco"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", firstName + " " + lastName)))
                                .put(Emailv31.Message.SUBJECT, "Team Invitation")
                                .put(Emailv31.Message.HTMLPART, emailBody)
                                .put(Emailv31.Message.CUSTOMID, "InvitationEmail")));

        try {
            MailjetResponse response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

