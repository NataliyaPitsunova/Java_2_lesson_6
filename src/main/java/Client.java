import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8180;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        readMessages();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String text = scanner.nextLine();
                        if (text.equals("/end")) {
                            sendMessage(text);
                            closeConnection();
                        } else {
                            sendMessage(text);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void closeConnection() {
        try {
            out.writeUTF("/end");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(1);
    }

    private void readMessages() throws IOException {
        while (true) {
            String messageInChat = in.readUTF();
            System.out.println(messageInChat);
            if (messageInChat.equals("/end")) {
                return;
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Client();
    }
}
