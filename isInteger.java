package data_ayam;

public class isInteger {
    public boolean check (String gas) {
        try {
            Integer.parseInt(gas);
            return true;
        }

        catch (NumberFormatException e) {
            // ga perlu ada pesan apa apa kalau misalnya emang false
            return false;
        }
    }
}
