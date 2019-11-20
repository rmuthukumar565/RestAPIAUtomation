package aeo.services.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {

	public <T> T getObjFromJsonWithknown(Class<T> clazz, String response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper.readValue(response, clazz);
	}

	public <T> T getObjFromJsonWithUnknown(Class<T> clazz, String response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(response, clazz);
	}

	public <T> String getJsonFromObjIncludeNotNull(T object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

	public <T> String getJsonFromObjIncludeNotEmpty(T object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		return mapper.writeValueAsString(object);
	}

	public <T> String getJsonFromObjIncludeAll(T object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.ALWAYS);
		return mapper.writeValueAsString(object);
	}

	public <T> T convertJsonToJava(Class<T> obj, String jsonResp) {
		Gson gson = new Gson();
		return gson.fromJson(jsonResp, obj);
	}

	public String convertJavaToJson(Object javaObj) {
		Gson gson = new Gson();
		return gson.toJson(javaObj);
	}

	public <T> List<T> convertJsonToJavaObjList(Class<T> obj, String jsonResp) {
		Gson gson = new Gson();
		JsonElement json = new JsonParser().parse(jsonResp);
		JsonArray jsonArray = json.getAsJsonArray();
		Iterator<JsonElement> arrayItr = jsonArray.iterator();
		List<T> tList = new ArrayList<T>();
		while (arrayItr.hasNext()) {
			JsonElement jsonElement = arrayItr.next();
			T tObj = gson.fromJson(jsonElement, obj);
			tList.add(tObj);
		}
		return tList;
	}
	//Story:2546
	public <T> String getJsonFromObjIncludeNotDefault(T object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_DEFAULT);
		return mapper.writeValueAsString(object);
	}
}
