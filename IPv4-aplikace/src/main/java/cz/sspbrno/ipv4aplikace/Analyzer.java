/**
 * Analyzer class analyzes IPv4 address
 * It determines: whether it is a public or non-public IP address,
 * then determines the class address of the network, subnet address,
 * network number, subnet number and PC number.
 *
 *
 * @author Martin Muzik
 * @version 1.0
 * @since 2022-04-24
 */

package cz.sspbrno.ipv4aplikace;

public class Analyzer {

    public static String startAnalyze(String address) {
        int[] ipAddressList = new int[5];

        if (checkInput(address, ipAddressList)) {
            IPAddress ipAddress = new IPAddress(
                    ipAddressList[0],
                    ipAddressList[1],
                    ipAddressList[2],
                    ipAddressList[3],
                    ipAddressList[4]);
            setClass(ipAddress);
            setRange(ipAddress);
            setSubnetAddress(ipAddress);
            return "Třída "+ipAddress.getIpClass()+" - "+ipAddress.getIpRange() +
                    "\nTřídní adresa sítě: "                                    +
                    "\nAdresa podsítě: "+ipAddress.getSubnetAddress();
        }
        return "Neplatný vstup";

    }

    /*
        Checks if the input is correct.
        Appends each part of IP address into ipAddressList.
        Returns true if the input is correct.
     */
    public static boolean checkInput(String address, int[] ipAddressList) {
        char currentChar;
        String currentPart = "";
        int partsDone = 0;

        if (18 >= address.length() && address.length() >= 9) {  // condition for faster detection of wrong input
            for (int i = 0; i <= address.length(); i++) {
                if (i < address.length()) {
                    currentChar = address.charAt(i);

                    if (Character.isDigit(currentChar) &&
                            currentPart.length() < 4 &&
                            partsDone < 5) {
                        currentPart += currentChar;

                    } else if (currentChar == '.' &&
                            currentPart.length() >= 1 &&
                            partsDone < 3 &&
                            Integer.parseInt(currentPart) <= 255 &&  // interval of each part of IP address is 0-255
                            Integer.parseInt(currentPart) >= 0) {
                        ipAddressList[partsDone++] = Integer.parseInt(currentPart);
                        currentPart = "";

                    } else if (currentChar == '/' &&
                            currentPart.length() >= 1 &&
                            partsDone == 3 &&
                            Integer.parseInt(currentPart) <= 255 &&  // interval of each part of IP address is 0-255
                            Integer.parseInt(currentPart) >= 0) {
                        ipAddressList[partsDone++] = Integer.parseInt(currentPart);
                        currentPart = "";

                    } else return false;
                }
                // IP prefix check
                else {
                    if (currentPart.length() < 3 &&
                       currentPart.length() > 0 &&
                       partsDone == 4 &&
                       Integer.parseInt(currentPart) <= 32 &&  // prefix interval is 1-32
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

    /*
        Sets IP class.
     */
    public static void setClass(IPAddress ip) {
        if (ip.getPart1() >= 0 && ip.getPart1() < 128) {
            ip.setIpClass('A');
        }
        else if (ip.getPart1() >= 128 && ip.getPart1() < 192) {
            ip.setIpClass('B');
        }
        else if (ip.getPart1() >= 192 && ip.getPart1() < 224) {
            ip.setIpClass('C');
        }
        else if (ip.getPart1() >= 224 && ip.getPart1() < 240) {
            ip.setIpClass('D');
        }
        else if (ip.getPart1() >= 240 && ip.getPart1() <= 255) {
            ip.setIpClass('E');
        }
    }

    public static void setRange(IPAddress ip) {
        if (ip.getPart1() == 10) {
            ip.setIpRange("Neveřejná");
        }
        else if (ip.getPart1() == 169 &&
                 ip.getPart2() == 254) {
            ip.setIpRange("Neveřejná");
        }
        else if (ip.getPart1() == 172 &&
                 ip.getPart2() >= 16 &&
                 ip.getPart2() <= 31) {
            ip.setIpRange("Neveřejná");
        }
        else if (ip.getPart1() == 192 &&
                ip.getPart2() <= 168) {
            ip.setIpRange("Neveřejná");
        }
        else ip.setIpRange("Veřejná");
    }

    public static void setSubnetAddress(IPAddress ip) {
        int difference;
        String result;
        boolean finished = false;

        if (ip.getPrefix() == 32) {
            result = ip.getPart1() + "." +
                     ip.getPart2() + "." +
                     ip.getPart3() + "." +
                     ip.getPart4();
            ip.setSubnetAddress(result);
        }
        else if (ip.getPrefix() == 24) {
            result = ip.getPart1() + "." +
                    ip.getPart2() + "." +
                    ip.getPart3() + "." +
                    "0";
            ip.setSubnetAddress(result);
        }
        else if (ip.getPrefix() == 16) {
            result = ip.getPart1() + "." +
                    ip.getPart2() + "." +
                    "0"   + "."              +
                    "0";
            ip.setSubnetAddress(result);
        }
        else if (ip.getPrefix() == 8) {
            result = ip.getPart1() + "." +
                    "0"     + "."            +
                    "0"      + "."           +
                    "0";
            ip.setSubnetAddress(result);
        }
        else if (ip.getPrefix() == 0){
            result = "0"+ "." +
                    "0"+ "." +
                    "0"+ "." +
                    "0";
            ip.setSubnetAddress(result);
        }
        else {
            difference = 256 - getMaskPart(ip.getPrefix());
            int subnetPart = 0;
            int i = 0;

            if (ip.getPrefix() >= 25) {
                while (!finished) {
                    if (ip.getPart4() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = ip.getPart1() + "." +
                        ip.getPart2() + "." +
                        ip.getPart3() + "." +
                        subnetPart;
                ip.setSubnetAddress(result);
            }
            else if (ip.getPrefix() >= 17) {
                while (!finished) {
                    if (ip.getPart3() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = ip.getPart1() + "." +
                        ip.getPart2() + "."  +
                        subnetPart + "."      +
                        "0";
                ip.setSubnetAddress(result);
            }
            else if (ip.getPrefix() >= 9) {
                    while (!finished) {
                        if (ip.getPart2() < i) {
                            subnetPart = i - difference;
                            finished = true;
                        }
                        i += difference;
                    }
                result = ip.getPart1() + "." +
                        subnetPart + "."        +
                        "0"         + "."        +
                        "0";
                ip.setSubnetAddress(result);
            }
            else if (ip.getPrefix() >= 1){
                while (!finished) {
                    if (ip.getPart1() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = subnetPart+ "." +
                        "0" + "."       +
                        "0"  + "."      +
                        "0";
                ip.setSubnetAddress(result);
            }
        }
    }

    public static int getMaskPart(int prefix) {
        String binary;

        if (prefix >= 25) {
            binary = "1".repeat(prefix - 24) + "0".repeat(32-prefix);
        }
        else if (prefix >= 17) {
            binary = "1".repeat(prefix - 16) + "0".repeat(24-prefix);
        }
        else if (prefix >= 9) {
            binary = "1".repeat(prefix - 8) + "0".repeat(16-prefix);
        }
        else {
            binary = "1".repeat(prefix) + "0".repeat(8-prefix);
        }
        return Integer.parseInt(binary, 2);
    }
}
