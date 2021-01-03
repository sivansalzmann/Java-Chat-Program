package ex3.ChatProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI implements StringConsumer, StringProducer{

    //GUI components signUp
    private JFrame frameSignUp;
    private JButton submitButton;
    private JPanel panel1, panel2;
    private JLabel headSignUp;
    private JTextField clientDeatils;
    private String nickname ="";

    // GUI components chat
    private JFrame frameChat;
    private JButton sendButton;
    private JPanel  displayHead,writeText,displayText;
    private JLabel headChat;
    private JTextArea messageBoard;
    private JTextField textMessage;

    //connection
    private StringConsumer consumer;

    public ClientGUI() {
        initSignUp();
        initChat();
    }

    public void initSignUp() {
        // set signup window
        frameSignUp = new JFrame("Sign Up here");
        clientDeatils = new JTextField(20);
        headSignUp = new JLabel("Enter your nickname");
        panel1 = new JPanel();
        panel2 = new JPanel();
        submitButton = new JButton("Submit");
    }

    public void initChat() {
        // set chat window
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
        // set all the components to null after everybody left the chat
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
        // set all the components to null after user enter nickname and come to the chat
        frameSignUp.setVisible(false);
        clientDeatils = null;
        headSignUp = null;
        panel1 = null;
        panel2 = null;
        submitButton = null;
    }

    // set the UI of sign-up
    public void signUp() {
        //set background color to panel1 and panel 2
        panel1.setBackground(Color.PINK);
        panel2.setBackground(Color.PINK);

        frameSignUp.setLayout(new FlowLayout());

        //set the fonts in signup frame
        headSignUp.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        submitButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        submitButton.setBackground(Color.magenta);

        //add the text box,button and header to the panels
        panel1.add(headSignUp);
        panel2.add(clientDeatils);
        panel2.add(submitButton);

        //add the panels to the frame
        frameSignUp.add(panel1);
        frameSignUp.add(panel2);

        //set the size of the frame(window)
        frameSignUp.setSize(350, 200);

        //set the sign-up window to be visible
        frameSignUp.setVisible(true);

    }

    //set the UI of the chat
    public void chat() {
        //set the background colors
        displayText.setBackground(Color.PINK);
        writeText.setBackground(Color.PINK);
        sendButton.setBackground(Color.magenta);

        //set the fonts
        sendButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        headChat.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

        frameChat.setLayout(new FlowLayout());

        //add the text box,button and header to the places in the window
        displayHead.add(headChat);
        displayText.add(messageBoard);
        writeText.add(textMessage);
        writeText.add(sendButton);

        //add all panels(write and display) to the frame
        frameChat.add(displayHead);
        frameChat.add(displayText);
        frameChat.add(writeText);

        //set the size of the frame(window)
        frameChat.setSize(460, 650);

        //set the sign-up window to be visible
        frameChat.setVisible(true);
    }

    //display the sign-up window to the user
    public void runSignUp() {
        signUp();
        //when submit to the name click
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //save the nickname name the user sent
                nickname = clientDeatils.getText();
                //start the chat
                runChat();
            }
        });
    }
    //run the chat
    public void runChat() {
        try {
            //close sign-up window
            closeSignUp();
            //display the chat window
            chat();
            //open new socket and connect the user to this port
            Socket socket = new Socket("127.0.0.1",1400);
            ConnectionProxy connectionProxy = new ConnectionProxy(socket);
            connectionProxy.addConsumer(this);
            connectionProxy.consume(nickname);
            //start connectionProxy
            connectionProxy.start();

            //when send message button click
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //display the message to the window
                    String message = textMessage.getText();
                    try {
                        //put the name of the sender before the message
                        connectionProxy.consume(message);
                        textMessage.setText("");

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            //Register the listener for the form using the anonymous inner class
            frameChat.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        //when the user disconnect
                        connectionProxy.consume("disconnect");
                    } catch (IOException ioException) {}

                    super.windowClosing(e);
                    //close the window
                    closeChat();
                    //close the socket
                    closeConnection(socket);
                    System.exit(0);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void consume(String str) {
        // Only one thread can send a message at a time
        synchronized (messageBoard) {
            messageBoard.append(str + "\n");
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
