package com.kejso.paper.repository;

import com.kejso.paper.entity.MagazineBasicInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MagazineBasicInfoRepoTest {
    @Autowired
    private MagazineBasicInfoRepo repo;

    @Test
    public void findFirstByJournalNameContains_1() throws Exception {
        MagazineBasicInfo magazineBasicInfo = repo.findFirstByJournalName("农业工程学报");
        System.out.println(magazineBasicInfo);
        Assert.assertEquals(20, (int) magazineBasicInfo.getJournalWeight());
    }

    @Test
    public void findFirstByJournalNameContains_2() throws Exception {
        MagazineBasicInfo magazineBasicInfo = repo.findFirstByJournalName("计算机学报");
        System.out.println(magazineBasicInfo);
        Assert.assertEquals(20, (int) magazineBasicInfo.getJournalWeight());
    }

    @Test
    public void findFirstByJournalNameContains_3() throws Exception {
        MagazineBasicInfo magazineBasicInfo = repo.findFirstByJournalName("公共卫生与预防医学");
        System.out.println(magazineBasicInfo);
        Assert.assertNull(magazineBasicInfo);
    }

}