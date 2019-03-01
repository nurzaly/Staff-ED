package my.gov.ilpsdk.apps.stafed.Services;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import my.gov.ilpsdk.apps.stafed.model.Bahagian;
import my.gov.ilpsdk.apps.stafed.model.Staff;

@Database(entities = {Staff.class, Bahagian.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StaffDao staffDao();
    public abstract BahagianDao BahagianDao();
}
