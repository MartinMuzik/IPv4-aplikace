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

    public void setIpRange() {
        if (this.getPart1() == 10) {
            this.ipRange = "Neveřejná";
        }
        else if (this.getPart1() == 169 &&
                this.getPart2() == 254) {
            this.ipRange = "Neveřejná";
        }
        else if (this.getPart1() == 172 &&
                this.getPart2() >= 16 &&
                this.getPart2() <= 31) {
            this.ipRange = "Neveřejná";
        }
        else if (this.getPart1() == 192 &&
                this.getPart2() <= 168) {
            this.ipRange = "Neveřejná";
        }
        else this.ipRange = "Veřejná";

    }

    public String getSubnetAddress() {
        return subnetAddress;
    }

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
        else if (this.getPrefix() == 0){
            result = "0"+ "." +
                    "0"+ "." +
                    "0"+ "." +
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
}
