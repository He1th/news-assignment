package example.micronaut.utils;


import io.micronaut.json.tree.JsonNode;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Singleton
public class Utils {

    @Inject
    ObjectMapper mapper;

    public String loadJsonFromFile(String filePath) throws IOException {
        try(InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream("./news.json")){
//pass InputStream to JSON-Library, e.g. using Jackson
            JsonNode jsonNode = mapper.readValue(in, JsonNode.class);
            String jsonString = mapper.writeValueAsString(jsonNode);
            return jsonString;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        //byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        //return new String(bytes, StandardCharsets.UTF_8);
    }
}