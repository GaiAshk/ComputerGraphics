package edu.cg.menu;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.Shape;

public class GsonMaker<T>  implements JsonSerializer<T>, JsonDeserializer<T> {

	private static final String CLASSNAME = "CLASSNAME";
	private static final String DATA = "DATA";

	public T deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		JsonObject jsonObject = jsonElement.getAsJsonObject();
		JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
		String className = prim.getAsString();
		Class<T> klass = getObjectClass(className);
		return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
	}
	
	public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
		jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
		return jsonObject;
	}
	
	/****** Helper method to get the className of the object to be deserialized *****/
	@SuppressWarnings("unchecked")
	public Class<T> getObjectClass(String className) {
		try {
			return (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			throw new JsonParseException(e.getMessage());
		}
	}
	
	
	private static class GsonHolder {
		public static Gson gson;
		static {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder
			.setPrettyPrinting()
			.registerTypeAdapter(Light.class, new GsonMaker<Light>())
			.registerTypeAdapter(Shape.class, new GsonMaker<Shape>());

			gson = gsonBuilder.create();
		}
	}
	
	public static Gson getInstance() {
		return GsonHolder.gson;
	}
}