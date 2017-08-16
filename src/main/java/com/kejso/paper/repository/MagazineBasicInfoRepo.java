package com.kejso.paper.repository;

import com.kejso.paper.entity.MagazineBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of MagazineBasicInfo
 * Created by Lu Chenwei on 2017/8/13.
 */
public interface MagazineBasicInfoRepo extends JpaRepository<MagazineBasicInfo, Integer> {

    MagazineBasicInfo findFirstByJournalName(String journalName);
}
