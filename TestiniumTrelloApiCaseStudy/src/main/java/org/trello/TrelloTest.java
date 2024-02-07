package org.trello;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigReader;
import static io.restassured.RestAssured.given;

public class TrelloTest {

    private static final String API_TOKEN = ConfigReader.getProperty("APIToken");
    private static final String API_KEY = ConfigReader.getProperty("APIKey");
    private static final String BOARD_ENDPOINT = "/boards";
    private static final String LISTS_ENDPOINT = "/lists";
    private static final String CARDS_ENDPOINT = "/cards";


    @BeforeClass
    public static void setup(ITestContext context) {
        /*
        ITestContext arayüzü TestNG testlerinde testler arasında bilgi paylaşmak için kullanılan bir arayüzdür.
         */
        // Set up the base URI for RestAssured using the Trello base URL from configuration
        //Trello URL'ini RestAssured için temel URI'yi ayarla
        RestAssured.baseURI = ConfigReader.getProperty("TrelloBaseUrl");

        // Access the test suite to store and share common attributes across test methods
        //Test yöntemleri genelinde ortak özellikleri depolamak ve paylaşmak için test paketine erişin
        ISuite suite = context.getSuite();
        //bir veya daha fazla testi ve bu testlerin yapılandırmasını,
        // parametrelerini ve diğer özelliklerini gruplayan bir yapıdır

        // Set test attributes for reuse in different test methods
        //Farklı testlerde kullanmak üzere test niteliklerini ayarla
        suite.setAttribute("listName", "Trello List");
        suite.setAttribute("cardName2", "CEM Trello Card2");
        suite.setAttribute("boardName", "CEM YUKSEL Trello Board");
        suite.setAttribute("cardName1", "CEM Trello Card1");
    }


    @Test(priority = 1)
    //Trello üzerinde bir board oluşturdum
    public void createTrelloBoardAndStoreId(ITestContext context) throws InterruptedException {
        // Retrieve board name from suite attributes
        //Paket özelliklerinden pano adını al
        ISuite suite = context.getSuite();
        String boardName = (String) suite.getAttribute("boardName");

        // Create a Trello board and store the board ID in suite attributes
        // Bir Trello panosu oluşturun ve pano kimliğini paket niteliklerinde saklayın

        String boardId = given()
                //metoduyla bir HTTP isteği hazırlanır
                .contentType(ContentType.JSON)
                //contentType'i JSON olarak belirlenir
                .queryParams("name", boardName, "key", API_KEY, "token", API_TOKEN)
                // isteğin parametreleri queryParams metoduyla belirlenir
                .when() //metoduyla isteğin gönderilmesi sağlanır
                .post(BOARD_ENDPOINT)
                //post() metodu ile belirtilen endpointe bir POST isteği yapılır
                .then()
                //then() metodu istenen yanıtının kontrolü gerçekleştirilir
                .statusCode(200)
                //HTTP durum kodunun 200 (Başarılı) olup olmadığı kontrol edilir
                .extract().jsonPath().get("id");
                //extract(): Bu metod, yanıtı çıkarmak için kullanılır.
                // JSON yanıtın id özelliğine erişilir ve bu özelliğin değeri bir değişkene (boardId) atanır
        Thread.sleep(2000);
        suite.setAttribute("boardId", boardId);
                //boardId değeri TestNG test suite içinde bir nitelik olarak saklanır
    }


    @Test(dependsOnMethods = "createTrelloBoardAndStoreId")
    //borda ait bir liste olusturdum.
    public void createTrelloListOnBoardAndStoreId(ITestContext context) throws InterruptedException {
        // Retrieve list name and board ID from suite attributes
        // Paket liste adını ve pano kimliğini alın
        ISuite suite = context.getSuite();
        String listName = (String) suite.getAttribute("listName");
        String boardId = (String) suite.getAttribute("boardId");

        // Create a Trello list on the specified board and store the list ID in suite attributes
        // Belirtilen panoda bir Trello listesi oluşturun ve liste kimliğini paket niteliklerinde saklayın
        String listId = given()
                .contentType(ContentType.JSON)
                .queryParams("name", listName, "idBoard", boardId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(LISTS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created list ID in suite attributes for potential future use
        // Gelecekteki potansiyel kullanım için paket özelliklerinde oluşturulan liste kimliğini ayarlayın
        suite.setAttribute("listId", listId);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "createTrelloListOnBoardAndStoreId")
    //olusturulan boarda 1 adet kart actim
    public void createTrelloCard1OnListAndStoreId(ITestContext context) throws InterruptedException {
        // Retrieve card name and list ID from suite attributes
        // Paket özelliklerinden kart adını ve liste kimliğini al
        ISuite suite = context.getSuite();
        String cardName1 = (String) suite.getAttribute("cardName1");
        String listId = (String) suite.getAttribute("listId");

        // Create a Trello Card 1 on the specified list and store the card ID in suite attributes
        // Belirtilen listede bir Trello Kartı 1 oluşturun ve kart kimliğini paket niteliklerinde saklayın
        String cardId1 = given()
                .contentType(ContentType.JSON)
                .queryParams("name", cardName1, "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(CARDS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created Card 1 ID in suite attributes for potential future use
        // Gelecekteki potansiyel kullanım için paket özelliklerinde oluşturulan Kart 1 kimliğini ayarlayın
        suite.setAttribute("cardId1", cardId1);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "createTrelloCard1OnListAndStoreId")
    //olusturulan boarda 2. karti actim
    public void createTrelloCard2OnListAndStoreId (ITestContext context) throws InterruptedException {
        // Retrieve card name and list ID from suite attributes
        // Paket özelliklerinden kart adını ve liste kimliğini al
        ISuite suite = context.getSuite();
        String cardName2 = (String) suite.getAttribute("cardName2");
        String listId = (String) suite.getAttribute("listId");

        // Create a Trello Card 2 on the specified list and store the card ID in suite attributes
        // Belirtilen listede bir Trello Card 2 oluşturun ve kart kimliğini paket niteliklerinde saklayın
        String cardId2 = given()
                .contentType(ContentType.JSON)
                .queryParams("name", cardName2, "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .post(CARDS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("id");

        // Set the created Card 2 ID in suite attributes for potential future use
        // Gelecekteki potansiyel kullanım için paket özelliklerinde oluşturulan Kart 2 kimliğini ayarlayın
        suite.setAttribute("cardId2", cardId2);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "createTrelloCard2OnListAndStoreId")
    //olusturulan kartlardan birini rastgele guncelledim
    public void updateTrelloCardOnListRandomly(ITestContext context) throws InterruptedException {
        ISuite suite = context.getSuite();
        // Retrieve random card ID, list ID, and create an update request with new details
        // Rastgele kart kimliğini alın, kimliği listeleyin ve yeni ayrıntılarla bir güncelleme isteği oluşturun
        int tmp = (int) (Math.random() * 1) + 1;
        String cardID = tmp == 1 ? (String) suite.getAttribute("cardId1") : (String) suite.getAttribute("cardId2");
        String listId = (String) suite.getAttribute("listId");

        given()
                .contentType(ContentType.JSON)
                .queryParams("id", cardID, "name", "Trello Card Updated", "color", "blue", "idList", listId, "key", API_KEY, "token", API_TOKEN)
                .when()
                .put(CARDS_ENDPOINT + "/" + cardID)
                .then()
                .statusCode(200);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "updateTrelloCardOnListRandomly")
    //olusturulan kartlardan birini sildim
    public void deleteTrelloCardOnList(ITestContext context) throws InterruptedException {
        // Retrieve Card 1 ID from suite attributes and send a request to delete the card
        // Suite özelliklerinden Kart 1 kimliğini alın ve kartın silinmesi için bir istek gönderin
        ISuite suite = context.getSuite();
        String cardId1 = (String) suite.getAttribute("cardId1");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(CARDS_ENDPOINT + "/" + cardId1)
                .then()
                .statusCode(200);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "deleteTrelloCardOnList")
    //olusturulan kartlardan ikincisini sildim
    public void deleteTrelloCard2OnList(ITestContext context) throws InterruptedException {
        // Retrieve Card 2 ID from suite attributes and send a request to delete the card
        // Suite özelliklerinden Kart 2 kimliğini alın ve kartın silinmesi için bir istek gönderin
        ISuite suite = context.getSuite();
        String cardId2 = (String) suite.getAttribute("cardId2");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(CARDS_ENDPOINT + "/" + cardId2)
                .then()
                .statusCode(200);
        Thread.sleep(2000);
    }


    @Test(dependsOnMethods = "deleteTrelloCard2OnList")
    //olusturulan boardu sildim
    public void deleteTrelloBoardAndVerifyDeletion (ITestContext context) {
        // Retrieve Board ID from suite attributes and send a request to delete the board
        // Paket özniteliklerinden Pano kimliğini alın ve panoyu silmek için bir istek gönderin
        ISuite suite = context.getSuite();
        String boardId = (String) suite.getAttribute("boardId");

        given()
                .queryParams("key", API_KEY, "token", API_TOKEN)
                .when()
                .delete(BOARD_ENDPOINT + "/" + boardId)
                .then()
                .statusCode(200);
    }
}
