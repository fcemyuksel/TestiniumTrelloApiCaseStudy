package org.trello;

import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Order;
import utilities.ConfigReader;


public class TrelloTest {

    @Test (priority = 1)
    //createTrelloBoard
    public void createTrelloBoard() throws UnirestException{
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
                .queryString("name", ConfigReader.getProperty("boardName"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("boardId", jsonResponse.get("id").getAsString());
    }


    @Test (dependsOnMethods = "createTrelloBoard")
    //createTrelloListOnBoard
    public void createTrelloListOnBoard() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", ConfigReader.getProperty("listName"))
                .queryString("idBoard", ConfigReader.getProperty("boardId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        ConfigReader.setProperty("listId", jsonResponse.get("id").getAsString());
    }


    @Test (dependsOnMethods = "createTrelloListOnBoard")
    //createTrelloCard1OnList
    public void createTrelloCard1OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", ConfigReader.getProperty("cardName1"))
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId1", jsonResponse.get("id").getAsString());
    }

    @Test (dependsOnMethods = "createTrelloCard1OnList" )
    //createTrelloCard2OnList
    public void createTrelloCard2OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", ConfigReader.getProperty("cardName2"))
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId2", jsonResponse.get("id").getAsString());
    }


    @Test (dependsOnMethods = "createTrelloCard2OnList")
    //updateTrelloCardOnListRandomly
    public void updateTrelloCardOnListRandomly() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        String cardID;
        int tmp = (int) (Math.random() * 1) + 1;
        if (tmp == 1) {
            cardID = ConfigReader.getProperty("cardId1");
        } else {
            cardID = ConfigReader.getProperty("cardId2");
        }
        String urlUpdated = "https://api.trello.com/1/cards/" + cardID;

        HttpResponse<JsonNode> response = Unirest.put(urlUpdated)
                .header("Accept", "application/json")
                .queryString("id", cardID)
                .queryString("name", "Trello Card Updated")
                .queryString("color", "blue")
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asJson();

    }
    @Test (dependsOnMethods = "updateTrelloCardOnListRandomly")
   //deleteTrelloCardOnList
    public void deleteTrelloCardOnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId1"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();
    }

    @Test (dependsOnMethods = "deleteTrelloCardOnList")
    //deleteTrelloCard2OnList
    public void deleteTrelloCard2OnList() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId2"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

    }

    @Test (dependsOnMethods = "deleteTrelloCard2OnList")
    //deleteTrelloBoard
    public void deleteTrelloBoard() throws UnirestException, InterruptedException {
        Thread.sleep(500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + ConfigReader.getProperty("boardId"))
                .queryString("key", ConfigReader.getProperty("APIKey"))
                .queryString("token", ConfigReader.getProperty("APIToken"))
                .asString();

    }
}
