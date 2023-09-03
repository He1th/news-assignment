package example.micronaut.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import example.micronaut.models.Day;
import example.micronaut.models.News;
import example.micronaut.models.Schedule;
import example.micronaut.utils.Utils;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jdk.jshell.execution.Util;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Singleton
public class NewsService {


    public String findNews() throws IOException {
        Gson gson = new Gson();
        String result = "";

        try {
            FileReader reader = new FileReader("./src/main/resources/news.json");
            Map<String, List<News>> schedule = gson.fromJson(reader, new TypeToken<Map<String, List<News>>>() {}.getType());

            // Before creating the result, create a better working data.
            Map<String, List<News>> news = this.convertNewsJson(schedule);

            // Set results.
            for (Map.Entry<String, List<News>> entry : news.entrySet()) {
                String title = entry.getKey();
                result += title.substring(0, 1).toUpperCase() + title.substring(1) + ": ";

                int size = entry.getValue().size();
                if(size == 0) result += "Nothing aired today";

                for (int i = 0; i < size; i++) {
                    News item = entry.getValue().get(i);

                    // If is connected with the next item, add comma.
                    if(item.isConnected()){
                        result +=  ", " + item.getStart() + " - " + item.getEnd();
                    }else{
                        // If NOT connected, NOT the last and NOT the first then print /
                        if (i != 0) result += " / ";

                        result += item.getTitle() + " " + item.getStart() + " - " + item.getEnd() + "";
                    }
                }
                result += "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, List<News>> convertNewsJson(Map<String, List<News>> data){

        List<News> newsList = new ArrayList<>();
        Map<String, List<News>> result = new LinkedHashMap<>();

        int index = 0;
        for (Map.Entry<String, List<News>> entry : data.entrySet()) {

            String day = entry.getKey();
            // Create empty arrayList
            result.put(day, new ArrayList<>());
            Map<String, Integer> foundTitles = new LinkedHashMap<>();
            if (!entry.getValue().isEmpty()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    News news = entry.getValue().get(i);

                    // If shows begin, set start time.
                    if (news.getState().equals("begin")) {
                        news.setStart(this.getStartTime(news.getTime()));
                    }

                    // Set previus index.
                    int prevIndex = index - 1;

                    // Get previous news given that there exists an previus index.
                    News prevNews = (prevIndex >= 0 && newsList.size() > prevIndex) ? newsList.get(prevIndex) : null;
                    boolean isEnd = prevNews != null
                            && news.getState().equals("end")
                            && news.getTitle().equals(prevNews.getTitle());

                    // If news section already exists make sure that they are connected and put after each other.
                    if(foundTitles.containsKey(news.getTitle()) && !isEnd) {
                        Integer matchIndex = foundTitles.get(news.getTitle());
                        News nextItem = entry.getValue().get(i+1);
                        News customNews = new News();
                        customNews.setConnected(true);
                        customNews.setStart(this.getStartTime(news.getTime()));
                        customNews.setEnd(this.getEndTime(news.getTime(), nextItem.getTime()));
                        result.get(day).add(matchIndex, customNews);

                        // Update the key givin that there might be more news doing the day.
                        // foundTitles.put(news.getTitle(), i);
                        // Assome there exists on more item.
                        entry.getValue().remove(i+1);
                        continue;
                    }

                    // The news might be uniq, but we dont know. Place it into the foundTitles map.
                    foundTitles.put(news.getTitle(), i);

                    // Set default day.
                    news.setDay(day);

                    // If state is end, then dont add to the array.
                    // Update the previous news with start and end time.
                    if (isEnd) {
                        prevNews.setEnd(this.getEndTime(prevNews.getTime(), news.getTime()));
                    } else {
                        newsList.add(news);
                        result.get(day).add(news);
                        index++;
                    }


                }
            }
        }
        return result;
    }

    private String getStartTime(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;

        if(minutes == 00){
            return String.format("%d", hours);
        }
        return String.format("%d:%02d", hours, minutes);
    }

    private String getEndTime(int start, int end){
        // Given that the end time is lower that the start time i assume it starts the day after.
        int totalSecs = end > start ? start + Math.abs(end - start) : end;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        if(minutes == 00){
            return String.format("%d", hours);
        }
        return String.format("%d:%02d", hours, minutes);
    }

}
