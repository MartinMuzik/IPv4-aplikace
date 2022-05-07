package cz.sspbrno.ipv4aplikace;

/**
 * IPAddress class manages IPv4 address
 * It manages: whether it is a public or non-public IP address,
 * the class address of the network, subnet address,
 * network number, subnet number and PC number.
 *
 *
 * @author Martin Muzik
 * @version 1.0
 * @since 2022-04-24
 */
public class IPAddress {
    private final int part1;
    private final int part2;
    private final int part3;
    private final int part4;
    private final int prefix;
    private char ipClass;
    private String ipRange;
    private String subnetAddress;

    // TODO: zdokumentovat tuto classu; udelat zde toString; v startAnalyze do spravneho returnu dat toString
    /**
     * This constructor sets up the IPv4 address (x.x.x.x/y) and all information about it.
     * @param part1 This the first "x" in the address.
     * @param part2 This the second "x" in the address.
     * @param part3 This the third "x" in the address.
     * @param part4 This the last "x" in the address.
     * @param prefix This the prefix ("y" in the address).
     */
    public IPAddress(int part1, int part2, int part3, int part4, int prefix) {
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
        this.part4 = part4;
        this.prefix = prefix;
        this.setIpClass();
        this.setIpRange();
        this.setSubnetAddress();
    }

    public int getPart1() {
        return part1;
    }

    public int getPart2() {
        return part2;
    }

    public int getPart3() {
        return part3;
    }

    public int getPart4() {
        return part4;
    }

    public int getPrefix() {
        return prefix;
    }

    /**
     * This method is used to set IP class.
     * IP Classes are based on interval of the first part of the IP Address.
     * A: <0; 128)
     * B: <128; 192)
     * C: <192; 224)
     * D: <224; 240)
     * E: <240; 255>
     */
    public void setIpClass() {
        if (this.getPart1() >= 0 && this.getPart1() < 128) {
            this.ipClass = 'A';
        } else if (this.getPart1() >= 128 && this.getPart1() < 192) {
            this.ipClass = 'B';
        } else if (this.getPart1() >= 192 && this.getPart1() < 224) {
            this.ipClass = 'C';
        } else if (this.getPart1() >= 224 && this.getPart1() < 240) {
            this.ipClass = 'D';
        } else if (this.getPart1() >= 240 && this.getPart1() <= 255) {
            this.ipClass = 'E';
        }
    }

    public char getIpClass() {
        return ipClass;
    }

    public String getIpRange() {
        return ipRange;
    }

    // TODO: special IP addresses
    /**
     * This method is used to set if the address is private, public or special.
     */
    public void setIpRange() {
        // IPs in form 10.x.x.x are private
        if (this.getPart1() == 10) {
            this.ipRange = "Neveřejná";
        }
        // IPs in form 169.254.x.x are private
        else if (this.getPart1() == 169 &&
                this.getPart2() == 254) {
            this.ipRange = "Neveřejná";
        }
        // IPs in form 172.(16-31).x.x are private
        else if (this.getPart1() == 172 &&
                this.getPart2() >= 16 &&
                this.getPart2() <= 31) {
            this.ipRange = "Neveřejná";
        }
        // IPs in form 192.168.x.x are private
        else if (this.getPart1() == 192 &&
                this.getPart2() <= 168) {
            this.ipRange = "Neveřejná";
        }
        else this.ipRange = "Veřejná";

    }

    public String getSubnetAddress() {
        return subnetAddress;
    }
    /**
     * This method is used to set a subnet address based on the prefix
     * Variable difference is used to represent difference between
     * the number 256 and the mask part (see getMaskPart documentation).
     *
     * Networks are split to subnets by this "difference" number.
     * For example IP with prefix 31 (the difference is 2) has subnets:
     * x.x.x.0,
     * x.x.x.2,
     * x.x.x.4,
     * x.x.x.6,
     * etc.
     *
     * All address parts after that mask part are 0. For example:
     * for IP 10.30.43.130/13 the subnet address is 10.24.0.0
     *
     * For prefixes 32; 16 and 8 the difference is 0,
     * so the subnet address part will be same as the input.
     *
     * Subnet address is always smaller or same as the IP address.
     * For example 10.30.43.130/26 has subnet address 10.30.43.128
     * because difference for prefix 26 is 64.
     */
    public void setSubnetAddress() {

        int difference;
        String result;
        boolean finished = false;

        if (this.getPrefix() == 32) {
            result = this.getPart1() + "." +
                    this.getPart2() + "." +
                    this.getPart3() + "." +
                    this.getPart4();
            this.subnetAddress = result;
        }
        else if (this.getPrefix() == 24) {
            result = this.getPart1() + "." +
                    this.getPart2() + "." +
                    this.getPart3() + "." +
                    "0";
            this.subnetAddress = result;
        }
        else if (this.getPrefix() == 16) {
            result = this.getPart1() + "." +
                    this.getPart2() + "." +
                    "0"   + "."              +
                    "0";
            this.subnetAddress = result;
        }
        else if (this.getPrefix() == 8) {
            result = this.getPart1() + "." +
                    "0"     + "."            +
                    "0"      + "."           +
                    "0";
            this.subnetAddress = result;
        }
        else {
            difference = 256 - getMaskPart();
            int subnetPart = 0;
            int i = 0;

            if (this.getPrefix() >= 25) {
                while (!finished) {
                    if (this.getPart4() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = this.getPart1() + "." +
                        this.getPart2() + "." +
                        this.getPart3() + "." +
                        subnetPart;
                this.subnetAddress = result;
            }
            else if (this.getPrefix() >= 17) {
                while (!finished) {
                    if (this.getPart3() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = this.getPart1() + "." +
                        this.getPart2() + "."  +
                        subnetPart + "."      +
                        "0";
                this.subnetAddress = result;
            }
            else if (this.getPrefix() >= 9) {
                while (!finished) {
                    if (this.getPart2() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = this.getPart1() + "." +
                        subnetPart + "."        +
                        "0"         + "."        +
                        "0";
                this.subnetAddress = result;
            }
            else if (this.getPrefix() >= 1){
                while (!finished) {
                    if (this.getPart1() < i) {
                        subnetPart = i - difference;
                        finished = true;
                    }
                    i += difference;
                }
                result = subnetPart+ "." +
                        "0" + "."       +
                        "0"  + "."      +
                        "0";
                this.subnetAddress = result;
            }
        }
    }

    /**
     * This method is used to get a part of a mask (represented by the prefix)
     * whose next part is 0. For example if mask is 255.240.0.0, the part we
     * are looking for is 240.
     *
     * First we need to find which part of the mask we are looking for.
     * Prefix 0-8 means we are looking for the first part,
     * prefix 9-16 means we are looking for the second part,
     * prefix 17-24 means we are looking for the third part,
     * prefix 25-32 means we are looking for the last part.
     * Then we need to convert the prefix to a binary number (that represents a mask).
     * The prefix = total number of 1 in a binary number. Each part of the mask has
     * 8 digits in total.
     */
    public int getMaskPart() {
        String binary;

        if (this.getPrefix() >= 25) {
            binary = "1".repeat(this.getPrefix() - 24) + "0".repeat(32-this.getPrefix());
        }
        else if (this.getPrefix() >= 17) {
            binary = "1".repeat(this.getPrefix() - 16) + "0".repeat(24-this.getPrefix());
        }
        else if (this.getPrefix() >= 9) {
            binary = "1".repeat(this.getPrefix() - 8) + "0".repeat(16-this.getPrefix());
        }
        else {
            binary = "1".repeat(this.getPrefix()) + "0".repeat(8-this.getPrefix());
        }
        return Integer.parseInt(binary, 2);
    }

    @Override
    public String toString() {
        return "Třída "+this.getIpClass()+" - "+this.getIpRange() +
                "\nTřídní adresa sítě: " + "?"                    +  //TODO
                "\nAdresa podsítě: "+this.getSubnetAddress()      +
                "\nČíslo sítě: " + "?"                            +  //TODO
                "\nČíslo podsítě: " + "?"                         +  //TODO
                "\nČíslo PC: " + "?";                                //TODO
    }
}
