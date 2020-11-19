package dev.codescreen;

import java.math.BigDecimal;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codescreen.Requests.AuthorizationRequest;
import dev.codescreen.Requests.LoadRequest;
import dev.codescreen.Models.TransactionAmount;
import dev.codescreen.Responses.LoadResponse;
import dev.codescreen.ServiceInterfaces.ServerHealthService;
import dev.codescreen.Utils.BootstrapUtil;
import dev.codescreen.Utils.DebitOrCredit;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Add test cases for your coding test in here. All tests must use the Junit test framework.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApplicationTest {

  @LocalServerPort
  private int port;
  private URL url;
  @Autowired
  private TestRestTemplate template;
  @Autowired
  private MockMvc mvc;

  @BeforeAll
  public static void setupApplication(){
    // Necessary to mark the application server as started
    if (!BootstrapUtil.isServerSetup()) {
      BootstrapUtil.mainSetup();
    }
  }

  @BeforeEach
  public void setUp() throws Exception {
    this.url = new URL("http://localhost:" + port + "/");
  }

  /*
  * Application unit tests
  * */
  // Test GET /ping on a healthy server
  @Test
  public void testGET_ping_healthyServer() throws Exception {
    ServerHealthService.setHealthy(true);
    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.get("/ping")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    )
            .andExpect(status().isOk())
            .andReturn();
    JSONObject jsonObject = new JSONObject(new String(result.getResponse().getContentAsByteArray()));
    System.out.println("Result content: server response:  " + result.getResponse().getContentAsString());
  }

  // Test GET /ping on a healthy server
  @Test
  public void testGET_ping_unhealthyServer() throws Exception {
    ServerHealthService.setHealthy(false);
    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.get("/ping")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    )
            .andExpect(status().is(400))
            .andReturn();
    JSONObject jsonObject = new JSONObject(new String(result.getResponse().getContentAsByteArray()));
    System.out.println("Result content: server response:  " + result.getResponse().getContentAsString());
  }

  // Test PUT /load/{messageId} on a healthy server
  @Test
  public void testPUT_load_healthyServer() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    String messageId = "123";
    TransactionAmount transactionAmount = new TransactionAmount(new BigDecimal(10), "USD", DebitOrCredit.CREDIT);
    LoadRequest loadRequest = new LoadRequest(messageId, BootstrapUtil.user1Id, transactionAmount);
    String json = objectMapper.writeValueAsString(loadRequest);

    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.put("/load/" + messageId)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    )
            .andExpect(status().is(201))
            .andReturn();

    JSONObject loadResponseJSON = new JSONObject(new String(result.getResponse().getContentAsByteArray()));
    JSONObject balanceJSON = loadResponseJSON.getJSONObject("balance");
    assertEquals(balanceJSON.get("amount"), 109);

    // second request on same account
    String messageId2 = "124";
    TransactionAmount transactionAmount2 = new TransactionAmount(new BigDecimal(11), "USD", DebitOrCredit.CREDIT);
    LoadRequest loadRequest2 = new LoadRequest(messageId2, BootstrapUtil.user1Id, transactionAmount2);
    String json2 = objectMapper.writeValueAsString(loadRequest2);
    MvcResult result2 = mvc.perform(
            MockMvcRequestBuilders.put("/load/" + messageId)
                    .content(json2)
                    .contentType(MediaType.APPLICATION_JSON)
    )
            .andExpect(status().is(201))
            .andReturn();
    JSONObject loadResponseJSON2 = new JSONObject(new String(result2.getResponse().getContentAsByteArray()));
    JSONObject balanceJSON2 = loadResponseJSON2.getJSONObject("balance");
    assertEquals(balanceJSON2.get("amount"), 120);
  }

  // Test PUT /authorization/{messageId} on a healthy server
  @Test
  public void testPUT_auth() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    String messageId = "123";
    TransactionAmount transactionAmount = new TransactionAmount(new BigDecimal(100), "USD", DebitOrCredit.CREDIT);
    AuthorizationRequest authorizationRequest = new AuthorizationRequest(messageId, BootstrapUtil.user3Id, transactionAmount);
    String json = objectMapper.writeValueAsString(authorizationRequest);

    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.put("/authorization/" + messageId)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    )
            .andExpect(status().is(400))
            .andReturn();

    JSONObject loadResponseJSON = new JSONObject(new String(result.getResponse().getContentAsByteArray()));

    System.out.println("response " + loadResponseJSON.toString());
  }

  // Test PUT /authorization/{messageId} on unknown user/account
  @Test
  public void testPUT_auth_unknownUser() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    String messageId = "123";
    TransactionAmount transactionAmount = new TransactionAmount(new BigDecimal(100), "USD", DebitOrCredit.CREDIT);
    AuthorizationRequest authorizationRequest = new AuthorizationRequest(messageId, "unknownUserId", transactionAmount);
    String json = objectMapper.writeValueAsString(authorizationRequest);

    MvcResult result = mvc.perform(
            MockMvcRequestBuilders.put("/authorization/" + messageId)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
    )
            .andExpect(status().is(400))
            .andReturn();

    JSONObject loadResponseJSON = new JSONObject(new String(result.getResponse().getContentAsByteArray()));

    System.out.println("response " + loadResponseJSON.toString());
  }

}
