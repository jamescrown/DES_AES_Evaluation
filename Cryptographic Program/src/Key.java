public class Key {

    // Key class stores and handles all key operations, calculates subkeys on construction

    private String key = "";
    //binary values and binaryString hold the list of outputs
    private String[] binaryValues =  new String[256];
    private String binaryString = "";
    private byte[] byteForm = new byte[256];
    private String postPC1 = "";
    //list of Permutations
    private int[] PC1 = {57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};
    private int[] PC2 = {14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};
    //how many times to shift per subKey
    private int[] leftShiftAmount = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    private String[] Cn = new String[17];
    private String[] Dn = new String[17];
    private String[] CnDn = new String[16];
    private String[] kn = new String[16];

    Key(String input){
        key = input;
        convertToBin();
        calcPC1();
        calcC0D0();
        for(int i=0;i<16;i++){
            int lsNum = leftShiftAmount[i];
            //you only ever left shift once or twice
            if(lsNum==1){
                Cn[i+1] = leftShift(Cn[i]);
                Dn[i+1] = leftShift(Dn[i]);
            }else if(lsNum==2){
                Cn[i+1] = leftShift(leftShift(Cn[i]));
                Dn[i+1] = leftShift(leftShift(Dn[i]));
            }else{
                System.out.println("Error: left shift iterator");
            }
            //store teh final values in arrays
            CnDn[i] = Cn[i+1]+Dn[i+1];
            kn[i] = calcPC2(CnDn[i]);
        }
    }

    private void convertToBin(){
        //get the values in bytes
        byteForm = key.getBytes();
        for(int i=0;i<byteForm.length;i++){
            //we make an array of binary values
            binaryValues[i]=calcBin(byteForm[i]);  //calcBin takes an integer input and returns a string version
            while(binaryValues[i].length()<8){
                //if it does not use up all 8 bits then we must add 0's
                binaryValues[i] = "0"+ binaryValues[i];
            }
        }
        int j =0;
        while(binaryValues[j]!=null){
            //add them all into one simple string
            binaryString+=binaryValues[j];
            j++;
        }
    }
    private String calcBin(int input) {
        return Integer.toBinaryString(input);
    }
    private void calcPC1(){
        for (int aPC1 : PC1) {
            postPC1 += binaryString.charAt(aPC1 - 1);       //simple algorithm to permute the binaryString into PC2
        }
    }
    private String calcPC2(String input){
        String output= "";
        for (int aPC2 : PC2) {
            output += input.charAt(aPC2 - 1);
        }
        return output;
    }
    private void calcC0D0(){
        //split the String into C and D
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for(int i=0;i<56;i++){
            if(i<28){
                left.append(postPC1.charAt(i));
            }else {
                right.append(postPC1.charAt(i));
            }
        }
        Cn[0] = left.toString();
        Dn[0] = right.toString();
    }
    private String leftShift(String input){
        //left shift only requires us to move the front character to the back
        StringBuilder output = new StringBuilder();
        for(int i=1;i<input.length();i++){
            output.append(input.charAt(i));
        }
        output.append(input.charAt(0));
        return output.toString();
    }

    public String getKn(int i){
        return kn[i];
    }
}
