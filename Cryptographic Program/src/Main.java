//importing the processing library for the UI
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import controlP5.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

public class Main extends PApplet {
    private static PApplet processing;
    private PImage bg;                  //store the background image
    private ControlP5 cp5;
    private static String result;       //store the calculation result
    private static boolean outputBool = false;      //used so we know when to print a result
    private static boolean mouseOverEncrypt = false;
    private static boolean mouseOverDecrypt = false;
    private static boolean mouseOverCopy = false;
    private static boolean mouseOverFMessage = false;
    private static boolean mouseOverFKey = false;
    private File myInputFile;
    private BufferedReader bReader = null;
    private FileReader fReader = null;
    private static long elapsedTimeDES;
    private static long elapsedTimeAES;
    private static String desStats = "";
    private static String aesStats = "";
    private static boolean loadingScreen = false;


    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    //Main Calculation Function that takes in message,key and type(encrypt or decrypt)
    private static void algorithm(String inputMessage, String inputKey, int type) throws UnsupportedEncodingException {
        //creates both a key and a message

        String encryptedBin = "";
        String calculatedMessage = "";
        String AESresult = "";
        for(int i=0;i<10000;i++){
            //start count
            long start = System.nanoTime();
            Key myKey = new Key(inputKey);
            Message myMessage = new Message(inputMessage, myKey);
            //if type is 0 then we encrypt, text input , binary output
            //if type is 1 then we decrypt, binary input, text output
            myMessage.calculate(type);
            encryptedBin = myMessage.getEnBin();
            calculatedMessage = myMessage.getCalculatedMessage();
            //end count
            //System.out.println(System.nanoTime()-start);
            elapsedTimeDES += System.nanoTime() - start;
        }
        elapsedTimeDES = elapsedTimeDES/10000;


       // calculate AES
        for(int i=0;i<10000;i++){
            long start = System.nanoTime();
            AES myAES = new AES(inputMessage, inputKey, type);
            AESresult = myAES.getResult();
            elapsedTimeAES+=System.nanoTime()-start;
        }
        elapsedTimeAES=elapsedTimeAES/10000;
        if (type == 0) {
//            result = encryptedBin;
            result = AESresult;

            //try to create a file output
            try {
                FileWriter fileWriter = new FileWriter("EncryptionResults.txt");
                fileWriter.write(result);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //result = calculatedMessage;
            result = AESresult;
            try {
                FileWriter fileWriter = new FileWriter("DecryptionResults.txt");
                fileWriter.write(result);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setup() {
        PFont myFont = createFont("", 15);
        processing = this;
        frameRate(60);
        bg = loadImage("cryptoBack.jpeg");
        bg.resize(width, height);
        cp5 = new ControlP5(this);
        textAlign(CENTER);
        //here we add the many text fields and buttons
        cp5.addTextfield("MInput")
                .setPosition((float) width / 2.9f, (float) height / 2.3f)
                .setSize(width / 3, height / 24)
                .setFont(createFont("arial", 20))
                .setFocus(true)
                .setAutoClear(false)
                .setColor(color(255, 255, 255))
                .setLabel("")
        ;
        cp5.addTextfield("KInput")
                .setPosition((float) width / 2.9f, (float) height / 3.1f)
                .setSize(width / 3, height / 24)
                .setFont(createFont("arial", 20))
                .setAutoClear(false)
                .setColor(color(255, 255, 255))
                .setPasswordMode(true)
                .setLabel("")
        ;
        cp5.addBang("Compare Encryption")
                .setPosition((float) width / 2.9f, (float) height / 2.0f)
                .setSize(width / 8, height / 8)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(30)
        ;
        cp5.addBang("Compare Decryption")
                .setPosition((float) width / 1.8f, (float) height / 2.0f)
                .setSize(width / 8, height / 8)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(30)
        ;
        cp5.addTextarea("Output")
                .setPosition((float) width / 3.0f, (float) height / 1.55f)
                .setSize(width, height / 10)//height / 24)
                .setFont(createFont("arial", 20))
                .showScrollbar()
                .isScrollable()
        ;
        cp5.addBang("Output to Input")
                .setPosition((float) width / 2.18f, (float) height / 1.3f)
                .setSize(width / 9, height / 12)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(16)
        ;
        cp5.addBang("File Key")
                .setPosition((float) width / 3.0f, (float) height / 1.3f)
                .setSize(width / 9, height / 12)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(16)
        ;
        cp5.addBang("File Message")
                .setPosition((float) width / 1.72f, (float) height / 1.3f)
                .setSize(width / 9, height / 12)
                .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(16)
        ;
        cp5.addTextarea("DES Efficiency")
                .setPosition((float) width / 15, (float) height / 5)
                .setSize(width/6, height / 2)//height / 24)
                .setFont(createFont("arial", 30))
                .showScrollbar()
                .isScrollable()
        ;
        cp5.addTextarea("AES Efficiency")
                .setPosition((float) width / 1.2f, (float) height / 5)
                .setSize(width/6, height / 2)//height / 24)
                .setFont(createFont("arial", 30))
                .showScrollbar()
                .isScrollable()
        ;
        cp5.setFont(myFont);

    }


    //we draw and colour everything as well as have detectors for mouse positioning
    public void draw() {
        background(bg);
        fill(0, 0, 0, 200f);
        rect(width / 4, height / 4, width / 2, height / 1.6f);//center black background
        rect(width / 80, height / 8, width / 5, height / 1.6f);//
        rect(width / 1.27f, height / 8, width / 5, height / 1.6f);
        fill(255, 255, 255, 255f);
        textAlign(CENTER);
        textSize(32);
        text("Key:", (float) width / 1.95f, (float) height / 3.4f);
        text("Message:", width / 1.95f, height / 2.45f);

        text("DES", (float) width / 9, (float) height / 6);
        text("AES", (float) width / 1.135f, (float) height / 6);


        //parameters for button locations
        float encryptX1 = (float) width / 2.9f;
        float encryptX2 = encryptX1 + width / 8;
        float encryptY1 = (float) height / 2.0f;
        float encryptY2 = encryptY1 + height / 8;

        float decryptX1 = (float) width / 1.8f;
        float decryptX2 = decryptX1 + width / 8;
        float decryptY1 = (float) height / 2.0f;
        float decryptY2 = decryptY1 + height / 8;

        float copyX1 = (float) width / 2.18f;
        float copyX2 = copyX1 + width / 9;
        float copyY1 = (float) height / 1.3f;
        float copyY2 = copyY1 + height / 12;

        float fMessageX1 = (float) width / 3.0f;
        float fMessageX2 = fMessageX1 + width / 9;
        float fMessageY1 = (float) height / 1.3f;
        float fMessageY2 = fMessageY1 + height / 12;

        float fKeyX1 = (float) width / 1.72f;
        float fKeyX2 = fKeyX1 + width / 9;
        float fKeyY1 = (float) height / 1.3f;
        float fKeyY2 = fKeyY1 + height / 12;

        //check if mouse is over button , then set boolean accordingly
        mouseOverEncrypt = mouseX > encryptX1 && mouseX < encryptX2 && mouseY > encryptY1 && mouseY < encryptY2;
        mouseOverDecrypt = mouseX > decryptX1 && mouseX < decryptX2 && mouseY > decryptY1 && mouseY < decryptY2;
        mouseOverCopy = mouseX > copyX1 && mouseX < copyX2 && mouseY > copyY1 && mouseY < copyY2;
        mouseOverFMessage = mouseX > fMessageX1 && mouseX < fMessageX2 && mouseY > fMessageY1 && mouseY < fMessageY2;
        mouseOverFKey = mouseX > fKeyX1 && mouseX < fKeyX2 && mouseY > fKeyY1 && mouseY < fKeyY2;


        //if encrypt or decrpyt button is pressed we must print the answer to the screen
        if (outputBool) {
            textSize(14);
            if (result.length() > 64) {
                String temp = "";
                //the output for the string must accommodate large strings over 64 in length
                for (int i = 0; i < result.length(); i++) {
                    if ((i % 64 == 0) && i != 0) {
                        temp += "\n";
                    }
                    temp += result.charAt(i);
                }
                cp5.get(Textarea.class, "Output")
                        .setText(temp)
                        .showScrollbar()
                        .isScrollable()
                ;
            } else {
                cp5.get(Textarea.class, "Output").setText(result)
                ;
            }
        }
    }

    //scale to screen size
    public void settings() {
        fullScreen();
    }

    //when mouse is clicked, we encrypt or decrypt depending on where it is , these are mainly operated through booleans
    public void mouseClicked() {
        if (mouseOverFKey) {
            readFileInput(0);
        }
        if (mouseOverFMessage) {
            readFileInput(1);
        }
        if (mouseOverCopy) {
            cp5.get(Textfield.class, "MInput").setText(result);
        }
        String m = cp5.get(Textfield.class, "MInput").getText();
        int blockNum = m.length()/8;
        int blockNumAES = m.length()/128;
        if(m.length()%8!=0){
            blockNum++;
            blockNumAES++;
        }
        int decryptBlockNum = m.length()/64;
        int decryptBlockNumAES = m.length()/1024;
        if(m.length()%64!=0){
            decryptBlockNum++;
        }
        if(m.length()%1024!=0){
            decryptBlockNumAES++;
        }

        String k = cp5.get(Textfield.class, "KInput").getText();
        if (mouseOverEncrypt && (k.length() == 8) && (m.length() != 0)) {
            try {
                algorithm(m, k, 0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            outputBool = true;
            aesStats += "Character Length: "+m.length()+"\n\nBlocks: "+blockNumAES+"\n\nIterations: "+10000+"\n\nTime(nano): "+String.valueOf(elapsedTimeAES)+"\n----------------------------\n";
            desStats += "Character Length: "+m.length()+"\n\nBlocks: "+blockNum+"\n\nIterations: "+10000+"\n\nTime(nano): "+String.valueOf(elapsedTimeDES)+"\n----------------------------\n";
            cp5.get(Textarea.class, "AES Efficiency").setText(aesStats);
            cp5.get(Textarea.class, "DES Efficiency").setText(desStats);

        } else if (mouseOverDecrypt && (k.length() == 8) && (m.length() != 0)) {
            try {
                algorithm(m, k, 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            outputBool = true;
            aesStats+= "Binary Length: "+m.length()+"\n\nBlocks: "+decryptBlockNumAES+"\n\nIterations: "+10000+"\n\nTime(nano): "+String.valueOf(elapsedTimeAES)+"\n----------------------------\n";
            desStats+="Binary Length: "+m.length()+"\n\nBlocks: "+decryptBlockNum+"\n\nIterations: "+10000+"\n\nTime(nano): "+String.valueOf(elapsedTimeDES)+"\n----------------------------\n";
            cp5.get(Textarea.class, "AES Efficiency").setText(aesStats);
            cp5.get(Textarea.class, "DES Efficiency").setText(desStats);
        } else {
            outputBool = false;
        }
    }

    public void readFileInput(int type) {
        /*
        trigger the JFileChooser that reads input, saves a file and reads that file
        then depending on the type , the string is set as the message input or key input on the UI
         */

        JFileChooser myChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        myChooser.setDialogTitle("Pick a .txt or .dat File");
        myChooser.setAcceptAllFileFilterUsed(false);
        myChooser.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt", "dat", "jpg"));

        int returnValue = myChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(myChooser.getSelectedFile().getPath());
            myInputFile = myChooser.getSelectedFile();
        }
        String readString = "";
        try {
            fReader = new FileReader(myInputFile);
            bReader = new BufferedReader(fReader);

            String curr;

            while ((curr = bReader.readLine()) != null) {
                readString += curr;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
                if (fReader != null) {
                    fReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type == 1) {
            cp5.get(Textfield.class, "KInput").setText(readString);
        } else if (type == 0) {
            cp5.get(Textfield.class, "MInput").setText(readString);
        } else {
            System.out.println("File Error");
        }

    }
}