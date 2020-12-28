package ex3.ChatProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI implements StringConsumer, StringProducer{

    //GUI components signUp and chat
    private JFrame frameChat,frameSignUp;
    private JButton sendButton,submitButton;
    private JPanel panel1, panel2, displayHead,writeText,displayText;
    private JTextField clientDeatils;
    private JLabel headSignUp,headChat;
    private JTextArea messageBoard;
    private JTextField textMessage;
    //

    private String nickname ="";
    private StringConsumer consumer;

    public ClientGUI() {
        initChat();
        initSignUp();
    }

    public void initSignUp() {
        frameSignUp = new JFrame("Sign Up here");
        clientDeatils = new JTextField(20);
        headSignUp = new JLabel("Enter your nickname");
        panel1 = new JPanel();
        panel2 = new JPanel();
        submitButton = new JButton("Submit");
    }

    public void initChat() {
        frameChat = new JFrame("Start chatting");
        sendButton = new JButton("send");
        textMessage = new JTextField(40);
        messageBoard = new JTextArea(30,30);
        displayHead = new JPanel();
        displayText = new JPanel();
        writeText = new JPanel();
        headChat = new JLabel("Welcome to the chat!");
        messageBoard.setEditable(false);
    }

    public void closeChat() {
        frameChat = null;
        sendButton = null;
        textMessage = null;
        messageBoard = null;
        displayText = null;
        writeText = null;
        messageBoard = null;
        displayHead = null;
        headChat = null;
    }


    public void closeSignUp() {
        frameSignUp = null;
        clientDeatils = null;
        headSignUp = null;
        panel1 = null;
        panel2 = null;
        submitButton = null;
    }

    public void signUp() {

        panel1.setBackground(Color.PINK);
        panel2.setBackground(Color.PINK);

        frameSignUp.setLayout(new FlowLayout());

        headSignUp.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        submitButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        submitButton.setBackground(Color.magenta);

        panel1.add(headSignUp);
        panel2.add(clientDeatils);
        panel2.add(submitButton);

        frameSignUp.add(panel1);
        frameSignUp.add(panel2);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nickname = clientDeatils.getText();
            }
        });

        frameSignUp.setSize(350, 200);
        frameSignUp.setVisible(true);

    }

    public void chat() {

        displayText.setBackground(Color.PINK);
        writeText.setBackground(Color.PINK);

        sendButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        sendButton.setBackground(Color.magenta);

        frameChat.setLayout(new FlowLayout());

        headChat.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

        displayHead.add(headChat);
        displayText.add(messageBoard);
        writeText.add(textMessage);
        writeText.add(sendButton);

        frameChat.add(displayHead);
        frameChat.add(displayText);
        frameChat.add(writeText);

        frameChat.setSize(400, 300);
        frameChat.setVisible(true);
    }

    public static void main(String[] args) {

        ClientGUI clientGui = new ClientGUI();

        clientGui.signUp();

        while (true) {
            synchronized (clientGui.nickname) {
                if (clientGui.nickname != "")
                    break;
            }
        }

        try {
            clientGui.closeSignUp();
            clientGui.chat();
            Socket sock = new Socket("", 1300);
            ConnectionProxy connectionProxy = new ConnectionProxy(sock);


            clientGui.addConsumer(connectionProxy);
            connectionProxy.addConsumer(clientGui);
            connectionProxy.consume(clientGui.nickname);
            connectionProxy.run();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            clientGui.closeChat();
        }

    }


    @Override
    public void consume(String str) {
        // Only one thread can send a message
        // at a time
        synchronized (messageBoard) {
            messageBoard.append(str);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumer = sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumer = null;

    }
}
