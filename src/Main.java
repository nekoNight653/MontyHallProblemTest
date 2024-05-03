import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String prize = "Good investment";
    private static final String loss = "Goat";

    //I made this test of the monty hall problem

    //I link this video
    //https://www.youtube.com/watch?v=4Lb-6rxZxx0
    //It's the breakdown of Monty Hall Problem

    private static final String switched = "did switch";
    private static final String didntSwitch = "didn't switch";
    private static final HashMap<String, Integer> timesSwitched = new HashMap<>();
    private static final HashMap<String, Integer> timesWon = new HashMap<>();
    public static void main(String[] args) {
        timesSwitched.put(switched, 0);
        timesSwitched.put(didntSwitch, 0);
        timesWon.put(switched, 0);
        timesWon.put(didntSwitch, 0);

        String play = "y";
        while(play.toLowerCase().contains("y")) {
            HashMap<String, String> doors = generateDoors(100);

            String choice = chooseDoor(doors);

            revealExcessDoors(choice, doors);

            if(outputDoorList(doors.keySet())) System.out.println(" are your remaining choices");
            else System.out.println(" is your remaining choice");

            System.out.println("Would you like to switch to the other door? type y if yes");
            String confirmation = scanner.next();

            if(confirmation.toLowerCase().contains("y")) {
                choice = switchDoors(choice, doors);
                updateStatistics(switched, doors.get(choice).equals(prize));
            } else {
                updateStatistics(didntSwitch, doors.get(choice).equals(prize));
            }



            revealWinner(doors, choice);



            System.out.println("Play again type y if yes!");
            play = scanner.next();
        }
        System.out.println("\n\n\nYou final statistics are:");

        System.out.println("\nYou switched " + timesSwitched.get(switched) + " time(s)");
        System.out.println("You won by switching " + timesWon.get(switched) + " time(s)");

        System.out.println("\n You didn't switch " + timesSwitched.get(didntSwitch) + " time(s)");
        System.out.println("You won by not switching " + timesWon.get(didntSwitch) + " time(s)");


    }


    //Generates a map with x doors(Strings) and x - 1 failure options and one success (Also all strings)
    public static final String prizeObstructer = "Door ";
    //It's not of course not entirely random computers as far as I know can't do that, but it's pretty random
    //Though I think it tends towards higher numbers (Or who knows maybe it is perfectly random and the random is just producing that effect because we can never predict random!)
    //Perhaps I should set the chance to like a little less than the number of doors if it's a high amount of doors
    public static String prizeDoor = "";
    public static HashMap<String, String> generateDoors(int number){
        final HashMap<String, String> choices = new HashMap<>();

        ArrayList<String> prizes = new ArrayList<>();
        prizes.add(loss);
        prizes.add(prize);

        Random random = new Random();

        for (int i = 1; i < (number + 1); i++) {
            int index;

            //Whenever the prize gets added we remove the prize from the pool,
            // so if it's out of the pool we ignore all randoms and if's for choosing if it'll be a prize or not
            if(prizes.size() == 2) {

                //At this point we know the prize hasn't been added so if it's about to end the loop we add the prize
                if ((i + 1) == (number + 1)) {
                    index = 1;

                } else {
                    //A 1 in x chance where if it equals zero it sets it as the prize door
                    index = random.nextInt(number) == 0 ? 1 : 0;
                }
            } else {
                index = 0;
            }

            choices.put(prizeObstructer + i, prizes.get(index));
            //If it's index 1 which is the prize we remove the prize from the pool
            if(index == 1) {
                prizes.remove(index);
                prizeDoor = prizeObstructer + i;
            }

        }

        return choices;
    }

    //Just outputs the strings sent into it
    //If there are multiple strings it outputs "string, string, string, and string"
    //Otherwise it outputs "string"
    //Note it prints no new lines

    //Returns false if it doesn't have multiple strings to output,
    // and true if it does. This allows you to customize the output based on whether it should be plural or not
    public static boolean outputDoorList(Collection<String> input) {
        ArrayList<String> toOutput = new ArrayList<>(input);


        if(toOutput.size() > 1) {
            toOutput.sort(new AlphabeticalNumericalComparator());
            String finalDoor = toOutput.get(toOutput.size() - 1);
            toOutput.remove(toOutput.size() - 1);

            for(String door : toOutput) {
                System.out.print(door + ", ");
            }
            System.out.print("and " + finalDoor);
            return true;

        } else {
            System.out.print(toOutput.get(0));
            return false;
        }
    }

    public static String chooseDoor(HashMap<String, String> doors) {

        String choice;
        while(true) {

            System.out.println("Pick a door you have " + doors.keySet().size() +" doors. \n");

            if(outputDoorList(doors.keySet())) System.out.print(" are your options.\n");

            else System.out.println("is your option");
            System.out.println("Type the doors corresponding number to choose that door");

            choice = "Door " + scanner.next();

            //If it is in the keys shorthand for if it's 1, 2, or 3 we break the loop
            if(doors.containsKey(choice)) break;
            else System.out.println("The input must be a number you can't use any words that represent numbers");

        }

        System.out.println(choice + " chosen!");
        
        return choice;
    }

    public static String switchDoors(String currentChoice, HashMap<String, String> choices) {
        if(choices.size() != 2) {
            System.out.println("Tried to switch doors with more than 2 options left not sure what you want?");
            return chooseDoor(choices);
        }

        for(String door : choices.keySet()) {
            if(!door.equals(currentChoice)) {
                System.out.println(door + " choose");
                return door;
            }
        }
        //And this last part will never be reached but if it is
        System.out.println("Couldn't find non choose door so choose yourself");
        return chooseDoor(choices);
    }

    public static void revealExcessDoors(String chosenDoor, HashMap<String, String> doors) {
        System.out.println();

        Random random = new Random();

        ArrayList<String> keySetOrdered = new ArrayList<>(doors.keySet());
        keySetOrdered.remove(chosenDoor);

        ArrayList<String> removedDoors = new ArrayList<>();

        while(doors.size() > 2) {
            int result = random.nextInt(keySetOrdered.size());
            String key = keySetOrdered.get(result);

            //If it isn't the prize door we remove ("reveal") it
            if(!doors.get(key).equals(prize)) {
                removedDoors.add(key);
                doors.remove(key);
            }
            keySetOrdered.remove(result);

        }

        if(outputDoorList(removedDoors)) System.out.println(" are all " + loss + "s!");
        else System.out.print(" is a " + loss + "\n");

    }

    //Takes a String for if you did switch or not should be one of the constants switched or didntSwitch
    // and a boolean based on whether you chose the correct door
    //Then based on that updates the HashMaps for how many times you switch and how many of those times you won
    public static void updateStatistics(String switched, boolean won) {
        int increment = 0;
        if(won) increment = 1;

        timesSwitched.put(switched, (timesSwitched.get(switched) + 1));
        timesWon.put(switched, (timesWon.get(switched) + increment));

    }

    public static void revealWinner(HashMap<String, String> doors, String chosenDoor) {
        String result = doors.get(chosenDoor);
        System.out.println("And behind " + chosenDoor + " the door you chose!, is a " + result);

        if(result.equals(prize)) System.out.println("Congratulations!");
        else {
            System.out.println("The prize was in " + prizeDoor);
            System.out.println("Better luck next time, right?");
        }
    }


//    private static void testMethod() {
//        int runs = 0;
//        ArrayList<Integer> values = new ArrayList<>();
//        int summedValues = 0;
//
//        String play = "y";
//        while (play.contains("y")) {
//            System.out.println("Give me a numba why doncha? positive whole numbas only please");
//            int testSize = scanner.nextInt();
//            runs += testSize;
//
//            for(int i = 0; i < testSize; i++) {
//                HashMap<String, String> doors = generateDoors(100);
//
//                int loops = 0;
//                for(String surprise : doors.values()) {
//                    loops++;
//                    if(surprise.equals(prize)) {
//
//                        System.out.println(loops);
//                        values.add(loops);
//                        summedValues += loops;
//
//                    }
//
//                }
//            }
//            System.out.println("Now see here the numbers that just spammed the screen those were possible " + prize + "s you could-adve had");
//            System.out.println("Would ya like a list of em? includin any previous ones you got m'kay. Anything with a y will do fer yes");
//
//            if(scanner.next().equals("y")) {
//
//                System.out.println("\n" + values);
//
//                System.out.println("\nThat's all the values now just speak ta me once ya finish readin em m'kay?");
//                scanner.next();
//            }
//
//            System.out.println("Now would ya like to see all them averaged? Anything with a y will tell me yes, ya hear?");
//            if(scanner.next().contains("y")) {
//                System.out.println(summedValues / runs);
//
//                System.out.println("Thats one average there.. talk to me once you'r done starin at it's beauty kay");
//                scanner.next();
//            } else System.out.println("Too bad well next up ah right");
//            System.out.println("Now tell me would'ja like to go again? say anything with 'y' in it for yes y'hear me");
//            play = scanner.next();
//
//        }
//
//        System.out.println("Well too bad.. see ya next time.." +
//                "\nListen I'll give you all the back end variables just this time since I feel we had a good thing goin");
//
//
//        System.out.println("\n\n\n\n\n\nFirst here's all yer values\n\n" + values +
//                "\n\nAlright now while you look at all that glorious information I'll be headin out m'kay.. Don't forget to close the door");
//        System.out.println("Total runs an I don mean times through my shop I mean generated realities k is " + runs);
//        System.out.println("The summed value of all the prize locations you could have had is " + summedValues + "\n");
//        String door = scanner.nextLine().toLowerCase();
//        if(door.contains("door") && door.contains("close")) {
//            System.out.println("Thank ya kindly fer closin my door");
//        } else {
//            System.out.println("Didn't close my door eh I'll see ya later now the question is whether you'll see me?");
//        }
//    }
}