package my.gov.ilpsdk.apps.stafed.Services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import my.gov.ilpsdk.apps.stafed.model.Staff;

@Dao
public interface StaffDao {
    @Query("SELECT * FROM staff ORDER BY nama")
    List<Staff> getAllStaff();

    @Query("SELECT * FROM staff WHERE bahagian LIKE :bahagian ORDER BY ketua DESC, gred DESC")
    List<Staff> getAllStaffByGroup(String bahagian);

    @Query("SELECT * FROM staff WHERE email LIKE :email")
    Staff getStaf(String email);

    @Query("SELECT COUNT(id) FROM staff WHERE bahagian LIKE :bahagian")
    public String getCountStaff(String bahagian);

    @Query("UPDATE staff SET nama = :nama WHERE email LIKE :email")
    public void updateNama(String nama, String email);

    @Query("UPDATE staff SET ext = :ext WHERE email LIKE :email")
    public void updateExt(String ext, String email);

    @Query("UPDATE staff SET bahagian = :bahagian WHERE email LIKE :email")
    public void updateBahagian(String bahagian, String email);

    @Query("UPDATE staff SET no_mobile = :no_mobile WHERE email LIKE :email")
    public void updateMobile(String no_mobile, String email);

    @Query("UPDATE staff SET no_mobile2 = :no_mobile2 WHERE email LIKE :email")
    public void updateMobile2(String no_mobile2, String email);

    @Insert
    void insertAll(Staff... staff);

    @Query("DELETE FROM staff")
    void deleteStaff();

}
