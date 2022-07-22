package data_ayam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;


public class Main {
    static String lokasi_data = "/home/gabriel/projects/java/workspace/ayam_v2/data.txt";
    static File data = new File(lokasi_data);
    static int banyak_ayam;

    public static void main (String[] args) throws IOException {
        String nama_ayam;
        int banyak_telur;
        int banyak_anak;
        String operasi;

        System.out.println("--------------------------------------------------------------");
        System.out.println("                     Program Data Ayam");
        Scanner scan = new Scanner(System.in);
        
        while (true) {
            System.out.println("--------------------------------------------------------------");
            list();
            System.out.println("--------------------------------------------------------------");
            System.out.print(">>> ");
            operasi = scan.next();

            isInteger alat_check = new isInteger();
            
            if (alat_check.check(operasi)) {
                cek_data(Integer.parseInt(operasi));
            }

            else if (operasi.equals("add")) {
                System.out.println("--------------------------------------------------------------");
                System.out.print("Masukkan nama ayam         : ");
                scan.nextLine();
                nama_ayam = scan.nextLine();
                System.out.print("Masukkan banyak telur      : ");
                banyak_telur = scan.nextInt();
                System.out.print("Masukkan banyak anak ayam  : ");
                banyak_anak = scan.nextInt();
                System.out.print("Masukkan tanggal mengerami : ");
                scan.nextLine();
                String tanggal_mengerami = scan.nextLine();
                add(nama_ayam, banyak_telur, banyak_anak, tanggal_mengerami);
            }

            else if (operasi.equals("rm")) {
                System.out.println("--------------------------------------------------------------");
                System.out.print("Masukkan nomor ayam : ");
                int nomor = scan.nextInt() - 1;
                rm(nomor);
            }

            else if (operasi.equals("q")) {
                break;
            }

            else {
                System.out.println("--------------------------------------------------------------");
                System.out.println("Invalid input.");
            }
        }

        scan.close();
    }

    static void cek_data (int index) {
        try {
            Scanner alat_scan = new Scanner(data);
            int i = 1;

            while (alat_scan.hasNextLine()) {
                if (i == index) {
                    System.out.println("--------------------------------------------------------------");
                    String[] list_data = alat_scan.nextLine().split("\\|", 5);
                    System.out.println("Nama ayam                   : " + list_data[0]);
                    System.out.println("Banyak telur                : " + list_data[1]);
                    System.out.println("Banyak anak                 : " + list_data[2]);
                    System.out.println("Awal mengerami              : " + list_data[3]);
                    System.out.println("Kemungkinan menetas pada    : " + list_data[4]);
                    
                    SimpleDateFormat myformat = new SimpleDateFormat("E, dd MMMM yyyy");

                    Date date1 = myformat.parse(list_data[3]);
                    Instant date_sekarang = Instant.now();
                    Date date2 = Date.from(date_sekarang);

                    long selisih_milisekon = date2.getTime() - date1.getTime();
                    long selisih_hari = selisih_milisekon / (1000 * 60 * 60 * 24);

                    float persentasi = Float.valueOf(selisih_hari) / 21 * 100;

                    System.out.print("\n[");

                    for (int bar = 0; bar < 50; bar++) {
                        if (bar <= persentasi / 2) {
                            System.out.print("\u001B[32m" + "\u001B[42m" + "#" + "\u001B[0m");
                        }

                        else {
                            System.out.print("=");
                        }
                    }

                    System.out.printf("] %.3f %% \n", persentasi);
                }

                else {
                    alat_scan.nextLine();
                }

                i++;
            }

            alat_scan.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan.");
        }

        catch (ParseException e) {
            e.getStackTrace();
        }
    }

    static void add (String nama_ayam, int banyak_telur, int banyak_anak, String awal) {
        SimpleDateFormat format_waktu = new SimpleDateFormat("dd MM yyyy");
        char pembatas = '|';
        
        try {
            if (awal.equals("-")) {
                FileWriter tulis = new FileWriter(data, true);
                tulis.write(nama_ayam + pembatas + banyak_telur + pembatas + banyak_anak + pembatas + "-" + pembatas + "-" + "\n");
                tulis.close();
            }   

            else {
                Date tanggal_awal = format_waktu.parse(awal);
                Instant instant_tanggal_awal = tanggal_awal.toInstant();
                Instant instant_tanggal_menetas = instant_tanggal_awal.plus(Duration.ofDays(21));
                Date tanggal_menetas = Date.from(instant_tanggal_menetas);
    
                FileWriter tulis = new FileWriter(data, true);
                tulis.write(nama_ayam + pembatas + banyak_telur + pembatas + banyak_anak + pembatas + new SimpleDateFormat("E, dd MMMM yyyy").format(tanggal_awal) + pembatas + new SimpleDateFormat("E, dd MMMM yyyy").format(tanggal_menetas) + "\n");
                tulis.close();
            }
        }        

        catch (IOException e) {
            e.getStackTrace();
        }

        catch (ParseException e) {
            e.getStackTrace();
        }
    }

    static void list () {
        try {
            Scanner file_scanner = new Scanner(data);
            int i = 1;

            while (file_scanner.hasNextLine()) {
                System.out.print(i + ". ");
                String[] nama_ayam = file_scanner.nextLine().split("\\|", 3);
                System.out.println(nama_ayam[0]);
                i++;
            }
            
            banyak_ayam = i;

            if (data.length() == 0) {
                System.out.println("Data ayam masih kosong");
            }
            
            file_scanner.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File data tidak ditemukan!!");
        }
    }

    static void rm (int nomor) throws IOException {
        try {
            Scanner file_scanner = new Scanner(data);

            int banyak_line = 0;
            int i = 0;
    
            while (file_scanner.hasNextLine()) { // menghitung banyak line untuk digunakan sebagai banyak array
                banyak_line++;
                file_scanner.nextLine();
            }

            file_scanner.close();
            
            String[] list = new String[banyak_line];
            
            Scanner file_scanner2 = new Scanner(data);
            
            try {
                for (i = 0; i < banyak_line; i++) {
                    list[i] = file_scanner2.nextLine();
                }
            }

            finally {
                file_scanner2.close();
            }
            
            FileWriter tulis = new FileWriter(data, false);

            try {
                for (i = 0; i < banyak_line; i++) { // mengembalikan dari array ke dalam file tanpa mengikutsertakan string yang sudah dihapus
                    if (i != nomor) {
                        tulis.write(list[i] + "\n");
                    }
        
                    else {
                        continue;
                    }
                }
            }

            finally {
                tulis.close();
            }
        }

        catch (FileNotFoundException e) {
            e.getStackTrace();
        }

        catch (IOException e) {
            e.getStackTrace();
        }
    }
}
