package model;

/**
 * Abstract class Anggota - blueprint data keanggotaan perpustakaan.
 * Tabel: anggota.
 * Subclass: AnggotaAktif, AnggotaNonAktif.
 */
public abstract class Anggota {

    private String idAnggota; 
    private String nama;
    private String email;
    private String noTelepon;
    private String alamat;
    private String role;
    private String tipeAnggota; // enum DB: 'AKTIF' | 'NON_AKTIF'

    //Constructor
    public Anggota(String idAnggota, String nama, String email,
                   String noTelepon, String alamat, String role,
                   String tipeAnggota) {
        this.idAnggota   = idAnggota;
        this.nama        = nama;
        this.email       = email;
        this.noTelepon   = noTelepon;
        this.alamat      = alamat;
        this.role        = role;
        this.tipeAnggota = tipeAnggota;
    }

    public Anggota() {}

    //Abstract method
    public abstract String getDetail();

    //Getter & Setter
    public String getIdAnggota(){ 
        return idAnggota;
    }
    public void setIdAnggota(String idAnggota){
        this.idAnggota = idAnggota; 
    }

    public String getNama(){
        return nama; 
    }
    public void setNama(String nama){
        this.nama = nama; 
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email; 
    }

    public String getNoTelepon(){
        return noTelepon; 
    }
    public void setNoTelepon(String noTelepon){
        this.noTelepon = noTelepon; 
    }

    public String getAlamat(){
        return alamat; 
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role; 
    }

    public String getTipeAnggota(){
        return tipeAnggota; 
    }
    public void setTipeAnggota(String tipeAnggota){
        this.tipeAnggota = tipeAnggota; 
    }

    @Override
    public String toString(){
        return "Anggota{id=" + idAnggota + ", nama=" + nama
                + ", email=" + email + ", tipe=" + tipeAnggota + "}";
    }
}
