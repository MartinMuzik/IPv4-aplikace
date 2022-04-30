package cz.sspbrno.ipv4aplikace;

/**
 * Analyzer class analyzes IPv4 address
 * It checks if the input is correct IPv4 address,
 * If the input is correct, it will set up the IPAddress object
 * and its characteristics. Then returns all these information
 * about analyzed IPv4 address.
 *
 *
 * @author Martin Muzik
 * @version 1.0
 * @since 2022-04-24
 */
public class Analyzer {
    /**
     * This method is used to start analyze of an entered IP Address.
     * It sets up the IPAddress object and gather all information about it.
     * @param address This is user input - IPv4 address (x.x.x.x/y)
     * @return String This returns all information about the entered IP address.
     */
    public static String startAnalyze(String address) {
        /*  This list is initialized in checkInput method.
            First 4 items will be parts of IP and the last one will be prefix.
         */
        int[] ipAddressList = new int[5];

        /*  If checkInput method don't find any mistake in the input (address),
            it will start setting up the IPAddress.
         */
        if (checkInput(address.trim(), ipAddressList)) {
            IPAddress ipAddress = new IPAddress(
                    ipAddressList[0],
                    ipAddressList[1],
                    ipAddressList[2],
                    ipAddressList[3],
                    ipAddressList[4]);
            return "Třída "+ipAddress.getIpClass()+" - "+ipAddress.getIpRange() +
                    "\nTřídní adresa sítě: "                                    +
                    "\nAdresa podsítě: "+ipAddress.getSubnetAddress();
        }
        return "Neplatný vstup";

    }

    /**
     * This method is used to check if the input is correct.
     * Appends each part of IP address into ipAddressList.
     * @param address This is user input - IPv4 address (x.x.x.x/y)
     * @param ipAddressList an empty list of 5 items, the parts of the address
     *                      will be appended here.
     * @return true if the input is correct.
     */
    public static boolean checkInput(String address, int[] ipAddressList) {
        char currentChar;
        String currentPart = "";
        int partsDone = 0;

        /*
            A condition for faster detection of wrong input.
            IP address can't be shorter than 9 characters and
            longer than 18 characters.
         */
        if (18 >= address.length() && address.length() >= 9) {
            for (int i = 0; i <= address.length(); i++) {
                // Last repetition is for checking and appending the prefix to the list
                if (i < address.length()) {
                    currentChar = address.charAt(i);

                    if (Character.isDigit(currentChar) &&
                            currentPart.length() < 4 &&  // Each number can have maximum 3 digits
                            // IPv4 has 5 parts in total. 4 parts divided by 3 dots and an prefix after slash
                            partsDone < 5) {
                        currentPart += currentChar;

                    } else if (currentChar == '.' &&
                            currentPart.length() >= 1 && // to detect if there aren't two dots consecutively
                            partsDone < 3 &&  // IPv4 has 4 parts divided by 3 dots
                            // interval of each part of an IP address is 0-255
                            Integer.parseInt(currentPart) <= 255 &&
                            Integer.parseInt(currentPart) >= 0) {
                        ipAddressList[partsDone++] = Integer.parseInt(currentPart);
                        currentPart = "";

                    } else if (currentChar == '/' &&
                            currentPart.length() >= 1 && // to detect if there aren't two dots consecutively
                            partsDone == 3 && // There are 4 parts in total before slash, so this one must be fourth.
                            // interval of each part of an IP address is 0-255
                            Integer.parseInt(currentPart) <= 255 &&
                            Integer.parseInt(currentPart) >= 0) {
                        ipAddressList[partsDone++] = Integer.parseInt(currentPart);
                        currentPart = "";

                    } else return false;
                }
                // Prefix check
                else {
                    // Prefix can have only 1 or 2 digits.
                    if (currentPart.length() < 3 &&
                       currentPart.length() > 0 &&
                       partsDone == 4 &&
                       // Prefix interval is 1-32.
                       Integer.parseInt(currentPart) <= 32 &&
                       Integer.parseInt(currentPart) >= 1) {
                        ipAddressList[partsDone++] = Integer.parseInt(currentPart);
                    }

                    else return false;
                }
            }
            return true;
        }
        return false;
    }

}
