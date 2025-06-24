package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Venue;

@Repository
public interface M_VenueRepository extends JpaRepository<M_Venue, Long> {
    List<M_Venue> findByDistrict_DistrictId(Long districtId);

    @Query("SELECT v FROM M_Venue v WHERE v.district.districtId IN :districtIds")
    List<M_Venue> findByDistrict_DistrictIdIn(@Param("districtIds") List<Long> districtIds);
}
