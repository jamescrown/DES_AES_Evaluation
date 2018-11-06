@SuppressWarnings("ALL")
//Block.java stores each 64 bit block of data, runs calculations on them and returns outputs
public class Block {
    private int[] initialPerm = {58, 50, 42, 34, 26, 18, 10, 2,
                            60, 52, 44, 36, 28, 20, 12, 4,
                            62, 54, 46, 38, 30, 22, 14, 6,
                            64, 56, 48, 40, 32, 24, 16, 8,
                            57, 49, 41, 33, 25, 17,  9, 1,
                            59, 51, 43, 35, 27, 19, 11, 3,
                            61, 53, 45, 37, 29, 21, 13, 5,
                            63, 55, 47, 39, 31, 23, 15, 7};
    private int[] inversePerm = { 40, 8, 48, 16, 56, 24, 64, 32,
                                39, 7, 47, 15, 55, 23, 63 ,31,
                                38, 6, 46, 14, 54, 22, 62 ,30,
                                37, 5, 45, 13, 53, 21, 61 ,29,
                                36, 4, 44, 12, 52, 20, 60 ,28,
                                35, 3, 43, 11, 51, 19, 59 ,27,
                                34, 2, 42, 10, 50, 18, 58, 26,
                                33, 1, 41, 9, 49, 17, 57, 25};
    private String postInitial = "";
    private String postF = "";
    private String postInversePerm = "";
    private String binaryString;
    private Key keyObj;
    private String textOut = "";

    Block(String input, Key key){
        binaryString = input;
        keyObj = key;
    }

    public void calculate(int type){
        //run it through the initial permutation
        initialPermutation();
        //then run the new String through the f function
        CipherFunction f = new CipherFunction(postInitial, keyObj, type);
        postF = f.getFinal();
        //then run the f result throguh the inverse Permutation
        inversePermutation();
        //convert the string to text
        convertToText(postInversePerm);
    }

    private void initialPermutation(){
        for (int anInitialPerm : initialPerm) {
            postInitial += binaryString.charAt(anInitialPerm - 1);
        }
    }

    private void inversePermutation(){
        for (int anInversePerm : inversePerm) {
            postInversePerm += postF.charAt(anInversePerm - 1);
        }
    }

    private void convertToText(String input){
        /*
        this method loops through iterations of 8 characters,
        converts them to hex and then to ascii,
        adding them all to a final output text string
         */
        StringBuilder output = new StringBuilder();
        int i=0;
        while(i<input.length()){
            String temp = "";
            temp += input.charAt(i);
            temp += input.charAt(i+1);
            temp += input.charAt(i+2);
            temp += input.charAt(i+3);
            temp += input.charAt(i+4);
            temp += input.charAt(i+5);
            temp += input.charAt(i+6);
            temp += input.charAt(i+7);
            int binaryVal = Integer.parseInt(temp, 2);
            output.append(Character.toString((char) binaryVal));
            i+=8;
        }
        textOut = output.toString();
    }
    public String getInversePermOutput(){
        return postInversePerm;
    }
    public String getTextOut(){
        return textOut;
    }

}