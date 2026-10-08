package Controller;

import Model.PenyetorSampah;
import java.util.ArrayList;
import java.util.Scanner;

public class PenyetorCRUD {

    private ArrayList<PenyetorSampah> daftarPenyetor;
    private int nomorBerikutnya = 1;

    public PenyetorCRUD() {

        daftarPenyetor = new ArrayList<PenyetorSampah>();

        daftarPenyetor.add(new PenyetorSampah(buatIdBaru(), "Budi Santoso", "Jl. Mawar No. 5"));
        daftarPenyetor.add(new PenyetorSampah(buatIdBaru(), "Siti Aminah", "Jl. Melati No. 12"));
    }

    // ID penyetor dibuat otomatis, contoh PYT-001
    private String buatIdBaru() {

        String nomor = "" + nomorBerikutnya;

        while (nomor.length() < 3) {
            nomor = "0" + nomor;
        }

        nomorBerikutnya++;

        return "PYT-" + nomor;
    }

    private boolean namaSudahAda(String nama) {

        for (PenyetorSampah penyetor : daftarPenyetor) {
            if (penyetor.getNamaPenyetor().equalsIgnoreCase(nama)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<PenyetorSampah> getDaftarPenyetor() {
        return daftarPenyetor;
    }

    public void tambahPenyetor(Scanner scanner) {

        System.out.println("\n--- TAMBAH DATA PENYETOR ---");

        String nama = "";
        boolean namaValid = false;

        while (!namaValid) {

            nama = InputCek.cekNama(scanner, "Masukkan nama penyetor: ");

            if (namaSudahAda(nama)) {
                System.out.println("Nama penyetor sudah terdaftar!");
            } else {
                namaValid = true;
            }
        }

        String alamat = InputCek.cekTeks(scanner, "Masukkan alamat penyetor: ", 3, 50);

        String id = buatIdBaru();
        daftarPenyetor.add(new PenyetorSampah(id, nama, alamat));

        System.out.println("Penyetor berhasil ditambahkan dengan ID " + id);
    }

    public void tampilkanPenyetor() {

        System.out.println("\n--- DAFTAR PENYETOR ---");

        if (daftarPenyetor.size() == 0) {
            System.out.println("Data masih kosong!");
        } else {

            for (PenyetorSampah penyetor : daftarPenyetor) {
                System.out.println("------------------------------------");
                penyetor.tampilkanInfo();
            }

            System.out.println("------------------------------------");
        }
    }

    public PenyetorSampah cariPenyetor(String idPenyetor) {

        for (PenyetorSampah penyetor : daftarPenyetor) {
            if (penyetor.getIdPenyetor().equalsIgnoreCase(idPenyetor)) {
                return penyetor;
            }
        }

        return null;
    }
}