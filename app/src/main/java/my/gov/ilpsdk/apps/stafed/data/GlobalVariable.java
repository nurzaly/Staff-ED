package my.gov.ilpsdk.apps.stafed.data;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import my.gov.ilpsdk.apps.stafed.model.Staff;

public class GlobalVariable extends Application {

    private List<Staff> staff = new ArrayList<>();
    private boolean donwload_bahagian = false;
    private boolean donwload_staff = false;
    private String current_group = "no";

    public void setStaff(List<Staff> staff){
        this.staff = staff;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public boolean isDonwload_bahagian() {
        return donwload_bahagian;
    }

    public void setDonwload_bahagian(boolean donwload_bahagian) {
        this.donwload_bahagian = donwload_bahagian;
    }

    public boolean isDonwload_staff() {
        return donwload_staff;
    }

    public void setDonwload_staff(boolean donwload_staff) {
        this.donwload_staff = donwload_staff;
    }

    public String getCurrent_group() {
        return current_group;
    }

    public void setCurrent_group(String current_group) {
        this.current_group = current_group;
    }
}
