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
                                .put(Emailv31.Message.HTMLPART, "<h3>Dear " + firstName + ",</h3><br />You have been invited to join our team. Please accept or reject the invitation by clicking the links below.<br /><a href='" + acceptUrl + "'>Accept</a> | <a href='" + rejectUrl + "'>Reject</a>")
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

