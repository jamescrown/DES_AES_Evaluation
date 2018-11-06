import java.util.ArrayList;
//Message class holds an inputted message as well as a key , it converts the messages into suitable format and splits them into blocks

public class Message {
    private String plainText="";
    private String binaryString = "";

    private ArrayList<Block> blockChain = new ArrayList<>();        //stores each 64 bit block
    private Key keyObj;
    private String calculatedMessage="";
    private int algorithmType = 0;

    Message(String input, Key k) {
        plainText = input;
        keyObj = k;
    }

    private void prepare(){
        //on encryption( type 0 ) we must convert to binary
        //on decryption we only accept binary input
        if(algorithmType==0){
            changeToBinary();
        }else{
            binaryString = plainText;
        }
        //ensure message is in multiples of 64 bits
        while(binaryString.length()%64!=0) {
            binaryString += "0";
        }
        //split the message up into blocks
        for(int i=0;i<(binaryString.length()/64);i++){
            StringBuilder blockInput = new StringBuilder();
            for(int j=0;j<64;j++){
                blockInput.append(binaryString.charAt((64 * i) + j));
            }
            blockChain.add(new Block(blockInput.toString(), keyObj));
        }
    }
    public void calculate(int type) {
        algorithmType = type;
        prepare();      //run all preparations for the string
        for(Block b:blockChain){
            //run encryption or decryption on each block
            b.calculate(type);
        }
        //the calculated message is the accumulation of all outputs of the blockchain
        calculatedMessage=getTextAllBlocks();
    }

    private String getTextAllBlocks(){
        StringBuilder output = new StringBuilder();
        for(Block b:blockChain){
            output.append(b.getTextOut());
        }
        return output.toString();
    }

    private void changeToBinary(){
        String[] binaryValues =  new String[plainText.length()*8];
        byte[] byteForm = plainText.getBytes();
        for(int i = 0; i< byteForm.length; i++){
            binaryValues[i]=getBin(byteForm[i]);
            while(binaryValues[i].length()<8){
                binaryValues[i] = "0"+ binaryValues[i];
            }
        }
        int j =0;
        while(binaryValues[j]!=null){
            binaryString+=binaryValues[j];
            j++;
        }

    }
    private String getBin(int input) {
        return Integer.toBinaryString(input);
    }

    public String getCalculatedMessage(){
        return calculatedMessage;
    }
    public String getEnBin(){
        // in the case of encryption we do not want a text output , so we retrieve the binary output of each block
        StringBuilder output = new StringBuilder();
        for(Block b:blockChain){
            output.append(b.getInversePermOutput());
        }
        return output.toString();
    }
}
