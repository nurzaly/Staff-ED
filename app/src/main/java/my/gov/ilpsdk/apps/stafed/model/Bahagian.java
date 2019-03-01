package my.gov.ilpsdk.apps.stafed.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import my.gov.ilpsdk.apps.stafed.R;

@Entity
public class Bahagian implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "nama_bahagian")
    String nama_bahagian;

    @ColumnInfo(name = "tag")
    String tag;

    @ColumnInfo(name = "parent")
    int parent;

    public Bahagian(int id, String nama_bahagian, String tag, int parent) {
        this.id = id;
        this.nama_bahagian = nama_bahagian;
        this.tag = tag;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_bahagian() {
        return nama_bahagian;
    }

    public void setNama_bahagian(String nama_bahagian) {
        this.nama_bahagian = nama_bahagian;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int get_group_icon(){
        int group_cion = R.drawable.ic_add_group;
        switch (this.getTag()){
            case "pengarah":
                group_cion = R.drawable.pengarah;
                break;
            case "admin":
                group_cion = R.drawable.admin;
                break;
            case "bppl":
                group_cion = R.drawable.bppl;
                break;
            case "bkkl":
                group_cion = R.drawable.bkkl;
                break;
            case "cess":
                group_cion = R.drawable.cess;
                break;
            case "bppa":
                group_cion = R.drawable.bppa;
                break;
            case "bpsm":
                group_cion = R.drawable.bpsm;
                break;
            case "tkr":
                group_cion = R.drawable.tkr;
                break;
            case "tkt":
                group_cion = R.drawable.tkt;
                break;
            case "jle":
                group_cion = R.drawable.jle;
                break;
            case "ppu":
                group_cion = R.drawable.ppu;
                break;
            case "kim":
                group_cion = R.drawable.kim;
                break;
            case "auto":
                group_cion = R.drawable.auto;
                break;

        }
        return group_cion;
    }
}
