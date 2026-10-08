package Controller;

import Model.PetugasSampah;
import java.util.ArrayList;
import java.util.Scanner;

public class PetugasCRUD {

    private ArrayList<PetugasSampah> daftarPetugas;
    private int nomorBerikutnya = 1;

    public PetugasCRUD() {

        daftarPetugas = new ArrayList<PetugasSampah>();

        daftarPetugas.add(new PetugasSampah(buatIdBaru(), "Andi Pratama", "Petugas Penimbang"));
        daftarPetugas.add(new PetugasSampah(buatIdBaru(), "Dewi Lestari", "Petugas Pencatat"));
    }

    // ID petugas dibuat otomatis, contoh PTG-001
    private String buatIdBaru() {

        String nomor = "" + nomorBerikutnya;

        while (nomor.length() < 3) {
            nomor = "0" + nomor;
        }

        nomorBerikutnya++;

        return "PTG-" + nomor;
    }

    private boolean namaSudahAda(String nama) {

        for (PetugasSampah petugas : daftarPetugas) {
            if (petugas.getNamaPetugas().equalsIgnoreCase(nama)) {
                return true;
            }
        }

        return false;
    }

    public void tambahPetugas(Scanner scanner) {

        System.out.println("\n--- TAMBAH DATA PETUGAS ---");

        String nama = "";
        boolean namaValid = false;

        while (!namaValid) {

            nama = InputCek.cekNama(scanner, "Masukkan nama petugas: ");

            if (namaSudahAda(nama)) {
                System.out.println("Nama petugas sudah terdaftar!");
            } else {
                namaValid = true;
            }
        }

        String jabatan = InputCek.cekTeks(scanner, "Masukkan jabatan petugas: ", 3, 30);

        String id = buatIdBaru();
        daftarPetugas.add(new PetugasSampah(id, nama, jabatan));

        System.out.println("Petugas berhasil ditambahkan dengan ID " + id);
    }

    public void tampilkanPetugas() {

        System.out.println("\n--- DAFTAR PETUGAS ---");

        if (daftarPetugas.size() == 0) {
            System.out.println("Data masih kosong!");
        } else {

            for (PetugasSampah petugas : daftarPetugas) {
                System.out.println("------------------------------------");
                petugas.tampilkanInfo();
            }

            System.out.println("------------------------------------");
        }
    }

    public PetugasSampah cariPetugas(String idPetugas) {

        for (PetugasSampah petugas : daftarPetugas) {
            if (petugas.getIdPetugas().equalsIgnoreCase(idPetugas)) {
                return petugas;
            }
        }

        return null;
    }
}