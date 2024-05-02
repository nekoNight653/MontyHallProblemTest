import java.util.ArrayList;
import java.util.Comparator;

//Compares letters alphabetically and number numerically
//I only did this because I really wanted the doors organized,
// and I couldn't find a comparator that did this already
public class AlphabeticalNumericalComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        ArrayList<String> string1 = separate(o1);
        ArrayList<String> string2 = separate(o2);



        if(string1.size() > string2.size()) {
            for(int i = 0; i < string2.size(); i++) {
                String one = string1.get(i);
                String two = string2.get(i);

                int result = alphabeNumeriCompare(one, two);

                if(result != 0) return result;
            }
        } else {


            for(int i = 0; i < string1.size(); i++) {
                String one = string1.get(i);
                String two = string2.get(i);

                int result = alphabeNumeriCompare(one, two);

                if(result != 0) return result;
            }

        }

        return string1.size() - string2.size();
    }

    //Tests if the string is a number or not and compares them accordingly
    //For numbers it converts one and two into an int and then returns (one - two)
    //And for strings we use the default comparing
    public int alphabeNumeriCompare(String one, String two) {

        if(isNumberStart(one, 0) && isNumberStart(two, 0)) {

            int first;
            int second;

            try {
                first = Integer.parseInt(one);
                second = Integer.parseInt(two);
            } catch (NumberFormatException n) {
                n.printStackTrace();
                //If we can't parse them to numbers we just set them to the same so there's no difference,
                // and is can compare based on alter elements

                first = 0;
                second = 0;
            }

            return first - second;

        } else {
            return one.compareToIgnoreCase(two);
        }

    }

    //Separates the string into numbers and non-number strings
    public ArrayList<String> separate(String input) {
        ArrayList<String> separatedStrings = new ArrayList<>();

        StringBuilder sepString = new StringBuilder();
        boolean buildingNumber = Character.isDigit(input.charAt(0));

        for(int i = 0; i < input.length(); i++) {

            char currentChar = input.charAt(i);

            sepString.insert(sepString.length(), currentChar);
            if(i != input.length() - 1) {
                if (buildingNumber) {

                    if (!Character.isDigit(input.charAt(i + 1))) {

                        separatedStrings.add(sepString.toString());
                        sepString = new StringBuilder();
                        buildingNumber = false;

                    }

                } else {

                    if (isNumberStart(input, i + 1)) {

                        separatedStrings.add(sepString.toString());
                        sepString = new StringBuilder();
                        buildingNumber = true;

                    }
                }
            } else {
                separatedStrings.add(sepString.toString());
            }

        }
        if(separatedStrings.isEmpty()) separatedStrings.add(input);
        return separatedStrings;
    }

    //Just tests if the index is a number or if it's a - and the next one is a number
    public boolean isNumberStart(String input, int index) {
        if(index - 1 < input.length()) {
            return Character.isDigit(input.charAt(index)) || (input.charAt(index) == '-' && Character.isDigit(input.charAt(index + 1)));
        }
        return Character.isDigit(input.charAt(index));
    }
}
