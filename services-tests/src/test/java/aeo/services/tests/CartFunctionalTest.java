package aeo.services.tests;

import java.util.HashMap;

import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.fasterxml.jackson.core.JsonProcessingException;

import aeo.services.base.RestServiceClient;
import aeo.services.bean.Cart;
import aeo.services.util.JsonUtil;
import aeo.services.util.Softly;
import ru.yandex.qatools.allure.annotations.Step;

public class CartFunctionalTest extends RestServiceClient {
	
	private JsonUtil jutil;
	private String baseUrl="";
	private Cart cartReq;
	private String jsonReqPayld = "";
	private String response="";
	private String cartId;
	
	@BeforeStory
	@Step
	public void bStory() {
		jutil = new JsonUtil();	
		cartReq = new Cart();
		
	}
	 
	@Given("I am connected with add cart service at:$resourceCollection")
	@Step
	public void createPostServicePath(@Named("resourceCollection") String resourceCollection) {
		baseUrl = getSrvcRestUrlBldr(resourceCollection);
	}
	
	@When("I have skuId as $SKUID, productId as $PRODID, quantity as $QTY")
	@Step
	public void createPayload(@Named("SKUID") String skuId,@Named("PRODID") String productId,@Named("QTY") String qty) throws JsonProcessingException {
		cartReq.setProductId(productId);
		cartReq.setQty(Integer.valueOf(qty));
		cartReq.setSkuId(skuId);
		
		
	}
	
	@Then("I send POST request to service with $FORMAT format")
	@Step
	public void sendPostReq(@Named("FORMAT") String format) throws Exception {
		jsonReqPayld = jutil.getJsonFromObjIncludeAll(cartReq);
		response = createResource(baseUrl, jsonReqPayld, format, new HashMap<String, String>());
		System.out.println("Response:: "+response);
	}
	
	@Then("I expect the service to return $RESP status code")
	@Step
	public void verifyStatusCode(@Named("RESP") int responseCode) {
		Softly.assertEquals("Response StatusCode mismatch", responseCode, getHttpResponseCode());
		Softly.assertAll();
	}
	
	@Given("I am connected with get cart service at:$resourceCollection")
	@Step
	public void createGetService(@Named("resourceCollection") String resourceCollection) {
		baseUrl = getSrvcRestUrlBldr(resourceCollection);
	}
	       
	@Then("I send GET request to service with $FORMAT format")
	@Step
	public void sendGetReq(@Named("FORMAT") String format) throws Exception {
		response = getResource(baseUrl+cartId, format, new HashMap<String, String>());
		System.out.println("Response:: "+response);
	}
	
	@Then("I verify skuId as $SKUID, productId as $PRODID, quantity as $QTY in the response")
	@Step
	public void response(@Named("SKUID") String skuId,@Named("PRODID") String prodId,@Named("QTY") String qty) {
		Cart addCartSuccessresp = jutil.convertJsonToJava(Cart.class, response);
		System.out.println(addCartSuccessresp.getProductId());
		System.out.println(addCartSuccessresp.getSkuId());
		System.out.println(addCartSuccessresp.getCartId());
	}
	
	@Then("I store cartid")
	public void storeCartId() {
		Cart addCartSuccessresp = jutil.convertJsonToJava(Cart.class, response);
		cartId = addCartSuccessresp.getCartId();
	}

}
