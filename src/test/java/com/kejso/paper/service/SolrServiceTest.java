package com.kejso.paper.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SolrServiceTest {
    @Autowired
    private SolrService solrService;

    @Before
    public void setUp() throws Exception {
        solrService.init("paper_wanfang");
    }

    @Test
    public void getSolrPapers() throws Exception {
        List<Map<String, Object>> solrPapers = solrService.getSolrPapers(0, 10);
        for (Map<String, Object> solrPaper : solrPapers) {
            System.out.println(solrPaper.get("id"));
        }
    }

    @Test
    public void updateOnePagePaper() throws Exception {
        List<Map<String, Object>> solrPapers = solrService.getSolrPapers(10000, 1);
        System.out.println(solrPapers.get(0));
        solrService.updateSolrPaper(solrPapers);
    }


    @Test
    public void updateAllPagePaper() throws Exception {
        // Num Docs:14013844
//        int startPage = 2155; need redo:2155x1000
//        int startPage = 395; last 395 x 10000
        int startPage = 0;
        int endPage = 21;
        int size = 100000;
        for (int i = startPage; i < endPage; i += 1) {
            System.out.println("Page: " + i);
            List<Map<String, Object>> solrPapers = solrService.getSolrPapers(i, size);
            System.out.println("---------------");
            solrService.updateSolrPaper(solrPapers);
            System.out.println("===============================================");
        }
    }

}