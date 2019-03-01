package my.gov.ilpsdk.apps.stafed.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import my.gov.ilpsdk.apps.stafed.data.Constant;
@Entity
public class Staff implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "nama")
    String nama;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "ext")
    String ext;

    @ColumnInfo(name = "bahagian")
    String bahagian;

    @ColumnInfo(name = "no_mobile")
    String no_mobile;

    @ColumnInfo(name = "no_mobile2")
    String no_mobile2;

    @ColumnInfo(name = "jawatan")
    String jawatan;

    @ColumnInfo(name = "ketua")
    int ketua;

    @ColumnInfo(name = "gred")
    int gred;

    @ColumnInfo(name = "unit")
    int unit;

    @ColumnInfo(name = "updated")
    String updated;

    public Staff(int id, String nama, String email, String ext, String bahagian, String no_mobile, String no_mobile2, String jawatan, int ketua, int gred, int unit, String updated) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.ext = ext;
        this.bahagian = bahagian;
        this.no_mobile = no_mobile;
        this.no_mobile2 = no_mobile2;
        this.jawatan = jawatan;
        this.ketua = ketua;
        this.gred = gred;
        this.unit = unit;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBahagian() {
        return bahagian;
    }

    public void setBahagian(String bahagian) {
        this.bahagian = bahagian;
    }

    public String getNo_mobile() {
        return no_mobile;
    }

    public void setNo_mobile(String no_mobile) {
        this.no_mobile = no_mobile;
    }

    public String getNo_mobile2() {
        return no_mobile2;
    }

    public void setNo_mobile2(String no_mobile2) {
        this.no_mobile2 = no_mobile2;
    }

    public String getJawatan() {
        return jawatan;
    }

    public void setJawatan(String jawatan) {
        this.jawatan = jawatan;
    }

    public int getKetua() {
        return ketua;
    }

    public void setKetua(int ketua) {
        this.ketua = ketua;
    }

    public int getGred() {
        return gred;
    }

    public void setGred(int gred) {
        this.gred = gred;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getAvatar(String type){
        return Constant.URL_AVATAR  + type + "/" + this.email.split("@")[0] +".jpg";
    }
}
