import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private Socket socket = null; // содали объект socket
    private DataInputStream in;
    private DataOutputStream out;

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(8180)) {
            System.out.println("Сервер запущен, ожидается подключение клиента...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            in = new DataInputStream(socket.getInputStream()); //переменная на входной поток
            out = new DataOutputStream(socket.getOutputStream()); //переменная на исходящий поток
            Scanner sc = new Scanner(System.in);    //сканер

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
                            String text = sc.nextLine();
                            if (text.equals("/end")) {
                                break;
                            } else {
                                sendMessage(text);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        new Server();
    }
}