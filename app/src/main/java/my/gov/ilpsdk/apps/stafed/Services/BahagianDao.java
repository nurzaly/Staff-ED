package my.gov.ilpsdk.apps.stafed.Services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import my.gov.ilpsdk.apps.stafed.model.Bahagian;

@Dao
public interface BahagianDao {

    @Query("SELECT * FROM bahagian where parent = 0")
    public List<Bahagian> getAllBahagian();

    @Query("SELECT * FROM bahagian WHERE tag LIKE :tag")
    public Bahagian getBahagian(String tag);

    @Query("SELECT * FROM bahagian WHERE id = :unit")
    public Bahagian getUnit(int unit);

    @Insert
    void insertAll(Bahagian... bahagian);

    @Query("DELETE FROM bahagian")
    void deleteBahagian();
}
