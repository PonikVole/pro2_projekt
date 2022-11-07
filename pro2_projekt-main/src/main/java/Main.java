import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.database.DatabaseOperations;
import models.database.DbInitializer;
import models.database.JdbcDatabaseOperations;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String databaseUrl = "jdbc:derby:ChatClientDb_skC";

        DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
        dbInitializer.init();

        try{
            DatabaseOperations databaseOperations =
                    new JdbcDatabaseOperations(databaseDriver, databaseUrl);

            //databaseOperations.addMessage(new Message("Langer", "pokusný text"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //testChat
        ChatFileOperations chatFileOperations = new JsonChatFileOperations();
        ChatClient chatClient = new FileChatClient(chatFileOperations);
        MainFrame frame = new MainFrame(800,600, chatClient);

    }

    private static void huh() {
        InMemoryChatClient client1 = new InMemoryChatClient();
        client1.login("client1");
        client1.sendMessage("Hello");
        client1.sendMessage("Suckers");
        client1.logout();
    }
}
