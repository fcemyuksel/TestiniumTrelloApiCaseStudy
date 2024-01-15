package org.trello;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import utilities.ConfigReader;

public class TrelloTest {


    String APIKey = "84fc4150414f99009bcdba78f02fa3ee";
    String APIToken = "ATTAed1feee91d3e5479daa56d0bc7b43b4b01b3b71df363baa147bdbf60ed72ddec34D71F34";
    String boardName = "Testinium Board";
    String listName = "Testinium List";
    String cardName1 = "Testinium Card1";
    String cardName2 = "Testinium Card2";



    @Test
        //createTrelloBoard
    public void a() throws UnirestException, InterruptedException {

        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
                .queryString("name", boardName)
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("boardId", jsonResponse.get("id").getAsString());
    }

    @Test
    //createTrelloListOnBoard
    public void b() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", listName)
                .queryString("idBoard", ConfigReader.getProperty("boardId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

        System.out.println(response.getBody());
        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("listId", jsonResponse.get("id").getAsString());

    }


    @Test
    //createTrelloCard1OnList
    public void c() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName1)
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId1", jsonResponse.get("id").getAsString());

    }

    @Test
    //createTrelloCard2OnList
    public void d() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("name", cardName2)
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
        ConfigReader.setProperty("cardId2", jsonResponse.get("id").getAsString());


    }

    @Test
    //updateTrelloCardOnListRandomly
    public void e() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
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
                .queryString("name", "Updated Card")
                .queryString("color", "blue")
                .queryString("idList", ConfigReader.getProperty("listId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asJson();
    }

    @Test
    //deleteTrelloCardOnList
    public void f() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId1"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();
    }

    //deleteTrelloCard2OnList
    public void g() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + ConfigReader.getProperty("cardId2"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();
    }

    @Test
    //deleteTrelloBoard
    public void h() throws UnirestException, InterruptedException {
        Thread.sleep(2500);
        HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + ConfigReader.getProperty("boardId"))
                .queryString("key", APIKey)
                .queryString("token", APIToken)
                .asString();

    }
}

