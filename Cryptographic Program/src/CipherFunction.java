import java.util.ArrayList;

import static java.lang.Math.pow;
//Cipher function handles the S permutations , the f function etc
public class CipherFunction {
    private Key keyObj;
    private String output;
    private String[] Ln = new String[17];
    private String[] Rn = new String[17];

    private int[] primP = {16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};
    //three dimensional array , number of S, rows and columns
    private int[][][] s = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },

            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },

            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },

            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },

            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},

            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };
    private int EnDetype;
    private int[] ePerm = {32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    CipherFunction(String postInitialPerm, Key key, int type) {
        keyObj = key;
        EnDetype = type;
        /*
        set the initial L0 and R0
        then loop through the many subkeys
        calculating the Ln Rn
         */
        setLR(postInitialPerm);
        for(int i=0;i<16;i++){
            mainLoop(i);
        }
        //the final output is R16+L16
        output = Rn[16]+Ln[16];
    }

    private void setLR(String currString) {
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        //split teh input string into Left and Right
        for (int i = 0; i < 32; i++) {
            left.append(currString.charAt(i));
        }
        for (int i = 32; i < 64; i++) {
            right.append(currString.charAt(i));
        }
        Ln[0] = left.toString();
        Rn[0] = right.toString();
    }
    private void mainLoop(int currIndex){
        //Ln+1 = Rn
        Ln[currIndex+1] = Rn[currIndex];
        //this is where encryption and decryption differ
        // Rn = XOR(Ln,f(Rn,Kn))
        // in decryption Kn is replaced with K(N-n)... that is the opposite end of the subkey
        if(EnDetype==1){

            Rn[currIndex+1] = xOr(Ln[currIndex], f(Rn[currIndex], keyObj.getKn(15-currIndex)));
        }else{

            Rn[currIndex+1] = xOr(Ln[currIndex], f(Rn[currIndex], keyObj.getKn(currIndex)));
        }
    }

    private String xOr(String x, String y) {
        StringBuilder z = new StringBuilder();
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1' && y.charAt(i) == '1') {
                z.append('0');
            } else if (x.charAt(i) == '0' && y.charAt(i) == '0') {
                z.append('0');
            } else {
                z.append('1');
            }
        }
        return z.toString();
    }

    private String f(String oldR, String kn) {
        String newR = permE(oldR);
        String newRK1 = xOr(newR, kn);// 48 bits
        //split into 8 groups of 6 digits
        int i = 0;
        int sNum = 0;
        ArrayList<Integer> sValues = new ArrayList<>();
        while (i != 48) {
            StringBuilder row = new StringBuilder();
            StringBuilder column = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                if(j==0 || j==5){
                    row.append(newRK1.charAt(i + j));  //get string of first six "101101"
                }else{
                    column.append(newRK1.charAt(i + j));  //get string of first six "101101"
                }
            }
            int rowDec = binToDec(row.toString());//change from binary string to decimal int
            int colDec = binToDec(column.toString());
            sValues.add(s[sNum][rowDec][colDec]);
            i += 6;
            sNum++;
        }
        StringBuilder tempOutput = new StringBuilder();
        for( int myInt:sValues){
            tempOutput.append(getBin(myInt));
        }
        StringBuilder finalOutput= new StringBuilder();
        //final permutation
        for (int aPrimP : primP) {
            finalOutput.append(tempOutput.charAt(aPrimP - 1));
        }
        return finalOutput.toString();
    }

    private int binToDec(String input) {
        //converts a binary input to decimal
        int multi = 0;
        int value = 0;
        for (int i = input.length()-1; i >= 0; i--) {
            value += Character.getNumericValue(input.charAt(i)) * (pow(2, multi));
            multi++;
        }
        return value;
    }

    private String permE(String input) {
        //runs permutation E on any input
        StringBuilder output = new StringBuilder();
        for (int anEPerm : ePerm) {
            output.append(input.charAt(anEPerm - 1));
        }
        return output.toString();
    }

    private String getBin(int input) {
        StringBuilder output = new StringBuilder(Integer.toBinaryString(input));
        while(output.length()<4){
            output.insert(0, '0');
        }
        return output.toString();
    }
    public String getFinal(){
        return output;
    }
}