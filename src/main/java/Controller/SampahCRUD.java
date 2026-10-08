package Controller;

import Model.LayananDaurUlang;
import Model.PenyetorSampah;
import Model.PetugasSampah;
import Model.Sampah;
import Model.SampahAnorganik;
import Model.SampahOrganik;
import java.util.ArrayList;
import java.util.Scanner;

public class SampahCRUD {

    private ArrayList<Sampah> daftarSampah;
    private PenyetorCRUD penyetorCRUD;
    private PetugasCRUD petugasCRUD;
    private int nomorBerikutnya = 1;

    public SampahCRUD(PenyetorCRUD penyetorCRUD, PetugasCRUD petugasCRUD) {

        this.penyetorCRUD = penyetorCRUD;
        this.petugasCRUD = petugasCRUD;
        daftarSampah = new ArrayList<Sampah>();

        PenyetorSampah budi = penyetorCRUD.cariPenyetor("PYT-001");
        PenyetorSampah siti = penyetorCRUD.cariPenyetor("PYT-002");
        PetugasSampah andi = petugasCRUD.cariPetugas("PTG-001");
        PetugasSampah dewi = petugasCRUD.cariPetugas("PTG-002");

        daftarSampah.add(new SampahOrganik(buatIdBaru(), 5.0, budi, andi, "Ya", 30));
        daftarSampah.add(new SampahAnorganik(buatIdBaru(), 3.5, siti, dewi, "Ya", "Plastik"));
        daftarSampah.add(new SampahOrganik(buatIdBaru(), 2.0, siti, andi, "Ya", 45));
        daftarSampah.add(new SampahAnorganik(buatIdBaru(), 2.5, budi, dewi, "Ya", "Logam"));
    }

    private int buatIdBaru() {
        int id = nomorBerikutnya;
        nomorBerikutnya++;
        return id;
    }

    private String ulang(String simbol, int jumlah) {

        String hasil = "";

        for (int i = 0; i < jumlah; i++) {
            hasil += simbol;
        }

        return hasil;
    }

    private String tengah(String teks, int lebar) {

        int kiri = (lebar - teks.length()) / 2;
        String hasil = ulang(" ", kiri) + teks;

        while (hasil.length() < lebar) {
            hasil += " ";
        }

        return hasil;
    }

    private String formatRupiah(double nilai) {
        return "Rp " + String.format("%,.0f", nilai);
    }

    private String garisTabel() {
        return "+" + ulang("-", 6)
                + "+" + ulang("-", 11)
                + "+" + ulang("-", 11)
                + "+" + ulang("-", 16)
                + "+" + ulang("-", 16)
                + "+" + ulang("-", 13)
                + "+" + ulang("-", 7) + "+";
    }

    private int hitungTotalPoin(PenyetorSampah penyetor) {

        int total = 0;

        for (Sampah sampah : daftarSampah) {
            if (sampah.getPenyetor().getIdPenyetor().equals(penyetor.getIdPenyetor())) {
                total += sampah.hitungPoinHijau();
            }
        }

        return total;
    }

    private String tentukanLevel(int poin) {

        if (poin >= 100) {
            return "Emas";
        } else if (poin >= 50) {
            return "Perak";
        } else {
            return "Perunggu";
        }
    }

    private PenyetorSampah pilihPenyetor(Scanner scanner) {

        penyetorCRUD.tampilkanPenyetor();

        PenyetorSampah penyetor = null;

        while (penyetor == null) {

            String id = InputCek.cekTeks(scanner, "Masukkan ID penyetor (contoh PYT-001): ", 3, 10);
            penyetor = penyetorCRUD.cariPenyetor(id);

            if (penyetor == null) {
                System.out.println("ID penyetor tidak ditemukan!");
            }
        }

        return penyetor;
    }

    private PetugasSampah pilihPetugas(Scanner scanner) {

        petugasCRUD.tampilkanPetugas();

        PetugasSampah petugas = null;

        while (petugas == null) {

            String id = InputCek.cekTeks(scanner, "Masukkan ID petugas (contoh PTG-001): ", 3, 10);
            petugas = petugasCRUD.cariPetugas(id);

            if (petugas == null) {
                System.out.println("ID petugas tidak ditemukan!");
            }
        }

        return petugas;
    }

    private void barisStruk(String label, String isi) {
        System.out.printf("| %-13s: %-21.21s |%n", label, isi);
    }

    private void cetakStruk(Sampah sampah) {

        String garis = "+" + ulang("-", 38) + "+";

        System.out.println();
        System.out.println(garis);
        System.out.println("|" + tengah("STRUK SETORAN SAMPAH", 38) + "|");
        System.out.println(garis);

        barisStruk("ID Sampah", "" + sampah.getIdSampah());
        barisStruk("Jenis", sampah.getJenisSampah());
        barisStruk("Berat", String.format("%.2f kg", sampah.getBerat()));
        barisStruk("Penyetor", sampah.getPenyetor().getNamaPenyetor());
        barisStruk("Petugas", sampah.getPetugas().getNamaPetugas());
        barisStruk("Nilai Setoran", formatRupiah(sampah.hitungNilaiSetoran()));
        barisStruk("Poin Hijau", "+" + sampah.hitungPoinHijau());

        int totalPoin = hitungTotalPoin(sampah.getPenyetor());
        barisStruk("Total Poin", totalPoin + " (" + tentukanLevel(totalPoin) + ")");

        System.out.println(garis);
        System.out.println("Terima kasih telah menyetor sampah!");
    }

    private void tampilkanDetail(Sampah sampah) {

        System.out.println("------------------------------------");

        // POLYMORPHISM: method yang berjalan sesuai jenis objek
        sampah.tampilkanInfo();
        sampah.cetakStatus();
        System.out.println("Nilai Setoran : " + formatRupiah(sampah.hitungNilaiSetoran()));

        // OVERLOADING: versi dengan parameter bonus untuk setoran besar
        if (sampah.getBerat() >= 10) {
            System.out.println("Nilai + Bonus 10% : " + formatRupiah(sampah.hitungNilaiSetoran(10)));
        }

        System.out.println("Poin Hijau    : " + sampah.hitungPoinHijau());
        System.out.println("------------------------------------");
    }

    public void tambahSampah(Scanner scanner) {

        System.out.println("\n--- TAMBAH DATA SAMPAH ---");

        String jenis = InputCek.cekJenis(scanner);
        double berat = InputCek.cekDesimal(scanner, "Masukkan berat (kg, 0.5 - 100): ", 0.5, 100);
        PenyetorSampah penyetor = pilihPenyetor(scanner);
        PetugasSampah petugas = pilihPetugas(scanner);

        if (jenis.equalsIgnoreCase("Organik")) {

            String mudahTerurai = InputCek.cekYaTidak(scanner,
                    "Apakah mudah terurai (Ya/Tidak): ");
            int hari = InputCek.cekAngka(scanner,
                    "Masukkan estimasi hari kompos (1 - 365): ", 1, 365);

            int id = buatIdBaru();
            Sampah baru = new SampahOrganik(id, berat, penyetor, petugas, mudahTerurai, hari);
            daftarSampah.add(baru);

            System.out.println("Data berhasil ditambahkan dengan ID " + id);
            cetakStruk(baru);

        } else {

            String dapatDidaurUlang = InputCek.cekYaTidak(scanner,
                    "Apakah dapat didaur ulang (Ya/Tidak): ");
            String bahan = InputCek.cekBahan(scanner);

            int id = buatIdBaru();
            Sampah baru = new SampahAnorganik(id, berat, penyetor, petugas, dapatDidaurUlang, bahan);
            daftarSampah.add(baru);

            System.out.println("Data berhasil ditambahkan dengan ID " + id);
            cetakStruk(baru);
        }
    }

    public void tampilkanSampah() {

        System.out.println("\n--- DAFTAR DATA SAMPAH ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        String garis = garisTabel();

        System.out.println(garis);
        System.out.printf("| %-4s | %-9s | %-9s | %-14s | %-14s | %11s | %5s |%n",
                "ID", "Jenis", "Berat", "Penyetor", "Petugas", "Nilai (Rp)", "Poin");
        System.out.println(garis);

        for (Sampah sampah : daftarSampah) {

            // POLYMORPHISM: hitungNilaiSetoran() dan hitungPoinHijau() sesuai jenis objek
            System.out.printf("| %-4d | %-9s | %6.2f kg | %-14.14s | %-14.14s | %,11.0f | %5d |%n",
                    sampah.getIdSampah(),
                    sampah.getJenisSampah(),
                    sampah.getBerat(),
                    sampah.getPenyetor().getNamaPenyetor(),
                    sampah.getPetugas().getNamaPetugas(),
                    sampah.hitungNilaiSetoran(),
                    sampah.hitungPoinHijau());
        }

        System.out.println(garis);
        System.out.println("Total data: " + daftarSampah.size());
        System.out.println("Gunakan menu 3 untuk melihat detail setiap data.");
    }

    public void updateSampah(Scanner scanner) {

        System.out.println("\n--- UPDATE DATA SAMPAH ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        int id = InputCek.cekAngka(scanner, "Masukkan ID sampah yang akan diupdate: ", 1, 9999);
        Sampah sampah = cariSampah(id);

        if (sampah == null) {
            System.out.println("Data tidak ditemukan!");
            return;
        }

        System.out.println("Data saat ini:");
        tampilkanDetail(sampah);

        double berat = InputCek.cekDesimal(scanner, "Masukkan berat baru (kg, 0.5 - 100): ", 0.5, 100);
        sampah.setBerat(berat);

        if (sampah instanceof SampahOrganik) {

            SampahOrganik organik = (SampahOrganik) sampah;

            String mudahTerurai = InputCek.cekYaTidak(scanner,
                    "Apakah mudah terurai (Ya/Tidak): ");
            int hari = InputCek.cekAngka(scanner,
                    "Masukkan estimasi hari kompos (1 - 365): ", 1, 365);

            organik.setMudahTerurai(mudahTerurai);
            organik.setEstimasiHariKompos(hari);

        } else if (sampah instanceof SampahAnorganik) {

            SampahAnorganik anorganik = (SampahAnorganik) sampah;

            String dapatDidaurUlang = InputCek.cekYaTidak(scanner,
                    "Apakah dapat didaur ulang (Ya/Tidak): ");
            String bahan = InputCek.cekBahan(scanner);

            anorganik.setDapatDidaurUlang(dapatDidaurUlang);
            anorganik.setJenisBahan(bahan);
        }

        System.out.println("Data berhasil diupdate!");
    }

    public void hapusSampah(Scanner scanner) {

        System.out.println("\n--- HAPUS DATA SAMPAH ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        int id = InputCek.cekAngka(scanner, "Masukkan ID sampah yang akan dihapus: ", 1, 9999);
        Sampah sampah = cariSampah(id);

        if (sampah == null) {
            System.out.println("Data engga ada bosq^_^!");
            return;
        }

        tampilkanDetail(sampah);

        String yakin = InputCek.cekYaTidak(scanner, "Beneran mau hapus data ini (Ya/Tidak): ");

        if (yakin.equalsIgnoreCase("Ya")) {
            daftarSampah.remove(sampah);
            System.out.println("Data berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public Sampah cariSampah(int idSampah) {

        for (Sampah sampah : daftarSampah) {
            if (sampah.getIdSampah() == idSampah) {
                return sampah;
            }
        }

        return null;
    }

    public void cariSampah(double beratMin, double beratMax) {

        int jumlah = 0;

        for (Sampah sampah : daftarSampah) {

            if (sampah.getBerat() >= beratMin && sampah.getBerat() <= beratMax) {
                tampilkanDetail(sampah);
                jumlah++;
            }
        }

        if (jumlah == 0) {
            System.out.println("Tidak ada data pada rentang berat tersebut!");
        } else {
            System.out.println("Ditemukan " + jumlah + " data.");
        }
    }

    public void cariDataSampah(Scanner scanner) {

        System.out.println("\n--- CARI / LIHAT DETAIL SAMPAH ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        System.out.println("1. Cari berdasarkan ID");
        System.out.println("2. Cari berdasarkan rentang berat");

        int pilihan = InputCek.cekAngka(scanner, "Pilih cara pencarian: ", 1, 2);

        if (pilihan == 1) {

            int id = InputCek.cekAngka(scanner, "Masukkan ID sampah: ", 1, 9999);
            Sampah sampah = cariSampah(id);

            if (sampah == null) {
                System.out.println("Data tidak ditemukan!");
            } else {
                tampilkanDetail(sampah);
            }

        } else {

            double min = InputCek.cekDesimal(scanner, "Berat minimal (kg): ", 0.5, 100);
            double max = InputCek.cekDesimal(scanner, "Berat maksimal (kg): ", min, 100);

            cariSampah(min, max);
        }
    }

    public void layananPengolahan(Scanner scanner) {

        System.out.println("\n--- LAYANAN PENGOLAHAN SAMPAH ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        int id = InputCek.cekAngka(scanner, "Masukkan ID sampah yang akan diolah: ", 1, 9999);
        Sampah sampah = cariSampah(id);

        if (sampah == null) {
            System.out.println("Data tidak ditemukan!");
            return;
        }

        if (sampah instanceof LayananDaurUlang) {

            LayananDaurUlang layanan = (LayananDaurUlang) sampah;

            sampah.tampilkanInfo();

            System.out.println("Biaya Pengolahan : " + formatRupiah(layanan.hitungBiayaPengolahan()));
            layanan.tampilkanPetunjuk();

        } else {
            System.out.println("Jenis sampah ini belum memiliki layanan pengolahan.");
        }
    }

    public void tampilkanPeringkat() {

        System.out.println("\n--- PERINGKAT PENYETOR ---");

        ArrayList<PenyetorSampah> urutan = new ArrayList<PenyetorSampah>();

        for (PenyetorSampah penyetor : penyetorCRUD.getDaftarPenyetor()) {
            urutan.add(penyetor);
        }

        if (urutan.size() == 0) {
            System.out.println("Data penyetor masih kosong!");
            return;
        }

        for (int i = 0; i < urutan.size() - 1; i++) {
            for (int j = 0; j < urutan.size() - 1 - i; j++) {

                if (hitungTotalPoin(urutan.get(j)) < hitungTotalPoin(urutan.get(j + 1))) {
                    PenyetorSampah dipindah = urutan.remove(j);
                    urutan.add(j + 1, dipindah);
                }
            }
        }

        String garis = "+" + ulang("-", 6)
                + "+" + ulang("-", 22)
                + "+" + ulang("-", 8)
                + "+" + ulang("-", 10) + "+";

        System.out.println(garis);
        System.out.printf("| %-4s | %-20s | %6s | %-8s |%n", "No", "Nama Penyetor", "Poin", "Level");
        System.out.println(garis);

        for (int i = 0; i < urutan.size(); i++) {

            int poin = hitungTotalPoin(urutan.get(i));

            System.out.printf("| %-4d | %-20.20s | %6d | %-8s |%n",
                    i + 1, urutan.get(i).getNamaPenyetor(), poin, tentukanLevel(poin));
        }

        System.out.println(garis);
        System.out.println("Level: Perunggu (< 50 poin), Perak (50 - 99 poin), Emas (100 poin ke atas).");
    }

    public void tampilkanRingkasan() {

        System.out.println("\n--- RINGKASAN SETORAN ---");

        if (daftarSampah.size() == 0) {
            System.out.println("Data masih kosong!");
            return;
        }

        int jumlahOrganik = 0;
        int jumlahAnorganik = 0;
        double beratOrganik = 0;
        double beratAnorganik = 0;
        double totalNilai = 0;
        int totalPoin = 0;

        for (Sampah sampah : daftarSampah) {

            if (sampah.getJenisSampah().equalsIgnoreCase("Organik")) {
                jumlahOrganik++;
                beratOrganik += sampah.getBerat();
            } else {
                jumlahAnorganik++;
                beratAnorganik += sampah.getBerat();
            }

            totalNilai += sampah.hitungNilaiSetoran();
            totalPoin += sampah.hitungPoinHijau();
        }

        double totalBerat = beratOrganik + beratAnorganik;
        int persenOrganik = (int) (beratOrganik / totalBerat * 100);
        int persenAnorganik = 100 - persenOrganik;
        int blokOrganik = persenOrganik / 5;
        int blokAnorganik = persenAnorganik / 5;

        System.out.printf("Sampah Organik   : %d data, total %.2f kg%n", jumlahOrganik, beratOrganik);
        System.out.printf("Sampah Anorganik : %d data, total %.2f kg%n", jumlahAnorganik, beratAnorganik);
        System.out.println("Total Nilai Setoran : " + formatRupiah(totalNilai));
        System.out.println("Total Poin Hijau    : " + totalPoin);

        System.out.println("\nKomposisi berat sampah:");
        System.out.println("Organik   : " + ulang("#", blokOrganik)
                + ulang("-", 20 - blokOrganik) + " " + persenOrganik + "%");
        System.out.println("Anorganik : " + ulang("#", blokAnorganik)
                + ulang("-", 20 - blokAnorganik) + " " + persenAnorganik + "%");
    }
}