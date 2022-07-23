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
    static File data = new File(lokasi_data); // membuat objek data sebagai representasi dari file data yang sudah dibuat sebelumnya
    static int banyak_ayam;

    public static void main (String[] args) throws IOException {
        String nama_ayam; // digunakan untuk meminta input nama ayam dari user
        int banyak_telur; // digunakan untuk meminta input banyak telur ayam dari user
        int banyak_anak; // digunakan untuk meminta input banyak anak ayam dari user
        String operasi; // digunakan sebagai tempat dari value operasi yang dapat dilakukan pada program ini (seperti add, remove, list, dll)

        System.out.println("--------------------------------------------------------------");
        System.out.println("                     Program Data Ayam");
        Scanner scan = new Scanner(System.in);
        
        while (true) {
            System.out.println("--------------------------------------------------------------");
            list(); // menampilkan list ayam yang tersedia di data. Kalau kosong ya kosong.
            System.out.println("--------------------------------------------------------------");
            System.out.print(">>> "); 
            operasi = scan.next(); // meminta input untuk operasi

            isInteger alat_check = new isInteger(); // melakukan pengecekan apakah input yang dimasukkan user dapat dikonversi ke integer. Kalau bisa nanti akan memiliki operasi khusus.
            
            if (alat_check.check(operasi)) {
                cek_data(Integer.parseInt(operasi)); // menampilkan data ayam di layar 
            }

            else if (operasi.equals("add")) {
                System.out.println("--------------------------------------------------------------");
                System.out.print("Masukkan nama ayam         : ");
                scan.nextLine();
                nama_ayam = scan.nextLine(); // meminta input nama ayam
                System.out.print("Masukkan banyak telur      : ");
                banyak_telur = scan.nextInt(); // meminta input banyak telur ayam
                System.out.print("Masukkan banyak anak ayam  : ");
                banyak_anak = scan.nextInt(); // meminta input banyak anak ayam
                System.out.print("Masukkan tanggal mengerami : ");
                scan.nextLine();  
                String tanggal_mengerami = scan.nextLine(); // meminta input tanggal start ayam mengerami yang dibutuhkan untuk menentukan tanggal perkiraan telur akan menetas dan melakukan perhitungan matematis
                add(nama_ayam, banyak_telur, banyak_anak, tanggal_mengerami); // memasukkan semua input user ke dalam file data.txt menggunakan method add
            }

            else if (operasi.equals("rm")) {
                System.out.println("--------------------------------------------------------------");
                System.out.print("Masukkan nomor ayam : ");
                int nomor = scan.nextInt() - 1;
                rm(nomor); // method rm ini digunakan untuk menghapus salah satu data ayam berdasarkan method list
            }

            else if (operasi.equals("q")) {
                break; // perintah ini digunakan untuk keluar dari program ini
            }

            else {
                System.out.println("--------------------------------------------------------------");
                System.out.println("Invalid input."); // apabila input tidak sesuai maka pesan ini akan muncul
            }
        }

        scan.close(); // mematikan scanner
    }

    static void cek_data (int index) {
        try {
            Scanner alat_scan = new Scanner(data);
            int i = 1;

            while (alat_scan.hasNextLine()) {
                if (i == index) {
                    System.out.println("--------------------------------------------------------------");
                    String[] list_data = alat_scan.nextLine().split("\\|", 5); // memisahkan data menjadi array index 5
                    System.out.println("Nama ayam                   : " + list_data[0]); // array index 0 merupakan nama ayam
                    System.out.println("Banyak telur                : " + list_data[1]); // array index 1 merupakan banyak telur
                    System.out.println("Banyak anak                 : " + list_data[2]); // array index 2 merupakan banyak anak
                    System.out.println("Awal mengerami              : " + list_data[3]); // array index 3 merupakan tanggal awal mengerami
                    System.out.println("Kemungkinan menetas pada    : " + list_data[4]); // array index 4 merupakan tanggal perkiraan menetas yang sudah di proses oleh program ini
                    
                    SimpleDateFormat myformat = new SimpleDateFormat("E, dd MMMM yyyy");

                    Date date1 = myformat.parse(list_data[3]); // mengonversi string menjadi date
                    Instant date_sekarang = Instant.now(); // mengonversi date menjadi instant
                    Date date2 = Date.from(date_sekarang); // mendapatkan tanggal sekarang

                    long selisih_milisekon = date2.getTime() - date1.getTime(); // mendapatkan selisih date2 dengan date1 tapi dalam satuan mili sekon
                    long selisih_hari = selisih_milisekon / (1000 * 60 * 60 * 24); // mengonversi dari mili sekon ke dalam satuan hari

                    float persentasi = Float.valueOf(selisih_hari) / 21 * 100; // mendapatkan persentasi progress penetasan telur

                    System.out.print("\n["); // membuat bagian awal dari progress barnya

                    for (int bar = 0; bar < 50; bar++) {
                        if (bar <= persentasi / 2) {
                            System.out.print("\u001B[32m" + "\u001B[42m" + "#" + "\u001B[0m"); // mencetak progress bar warna hijo
                        }

                        else {
                            System.out.print("="); // ini progress bar kosong belum diisi
                        }
                    }

                    System.out.printf("] %.3f %% \n", persentasi); // ini penutup progress barnya
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
            if (awal.equals("-")) { // ini untuk menuliskan data ayam tanpa memasukkan date
                FileWriter tulis = new FileWriter(data, true);
                tulis.write(nama_ayam + pembatas + banyak_telur + pembatas + banyak_anak + pembatas + "-" + pembatas + "-" + "\n"); // menuliskan data yang di input oleh user ke dalam file data.txt. Setiap data dipisahkan oleh "|"
                tulis.close();
            }   

            else { // ini untuk menuliskan data ayam dengan menggunakan date
                Date tanggal_awal = format_waktu.parse(awal); // ini untuk mengubah tanggal dalam bentuk String menjadi bentuk date
                Instant instant_tanggal_awal = tanggal_awal.toInstant(); // mengubah date menjadi instant
                Instant instant_tanggal_menetas = instant_tanggal_awal.plus(Duration.ofDays(21)); // menambahkan 21 hari dari tanggal instant
                Date tanggal_menetas = Date.from(instant_tanggal_menetas); // mengembalikan dari bentuk instant ke dalam bentuk date
    
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

    static void list () {  // untuk membaca data ayam yang ada di file data.txt. Kemudian menampilkannya di layar.
        try {
            Scanner file_scanner = new Scanner(data);
            int i = 1;

            while (file_scanner.hasNextLine()) {
                System.out.print(i + ". ");
                String[] nama_ayam = file_scanner.nextLine().split("\\|", 5); // memecah data menjadi array index 5
                System.out.println(nama_ayam[0]); // mengambil array index 0, karena nama ayam ada di situ
                i++;
            }
            
            banyak_ayam = i;

            if (data.length() == 0) {
                System.out.println("Data ayam masih kosong"); // teks ini ditampilkan kalau file data.txt masih kosong
            }
            
            file_scanner.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File data tidak ditemukan!!");
        }
    }

    static void rm (int nomor) throws IOException { // untuk menghapus salah satu data ayam
        try {
            Scanner file_scanner = new Scanner(data);

            int banyak_line = 0;
            int i = 0;
    
            while (file_scanner.hasNextLine()) { // menghitung banyak line untuk digunakan sebagai banyak array
                banyak_line++;
                file_scanner.nextLine();
            }

            file_scanner.close();
            
            String[] list = new String[banyak_line]; // inisialisasi banyak array
            
            Scanner file_scanner2 = new Scanner(data);
            
            try {
                for (i = 0; i < banyak_line; i++) {
                    list[i] = file_scanner2.nextLine(); // memasukkan semua data ke dalam array
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
