package cz.sspbrno.ipv4aplikace;

public class IPAddress {
    private final int part1;
    private final int part2;
    private final int part3;
    private final int part4;
    private final int prefix;
    private char ipClass;
    private String ipRange;
    private String subnetAddress;

    public IPAddress(int part1, int part2, int part3, int part4, int prefix) {
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
        this.part4 = part4;
        this.prefix = prefix;
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

    public void setIpClass(char ipClass) {
        this.ipClass = ipClass;
    }

    public char getIpClass() {
        return ipClass;
    }

    public String getIpRange() {
        return ipRange;
    }

    public void setIpRange(String ipRange) {
        this.ipRange = ipRange;
    }

    public String getSubnetAddress() {
        return subnetAddress;
    }

    public void setSubnetAddress(String subnetAddress) {
        this.subnetAddress = subnetAddress;
    }

}
