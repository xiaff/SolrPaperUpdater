package com.kejso.paper.service;

import com.kejso.paper.entity.MagazineBasicInfo;
import com.kejso.paper.repository.MagazineBasicInfoRepo;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Solr 查询和更新服务
 * Created by Lu Chenwei on 2017/8/13.
 */
@Component
public class SolrService {
    private SolrServer solrServer;
    public static final Logger logger = LoggerFactory.getLogger(SolrService.class);

    @Resource
    private MagazineBasicInfoRepo magazineBasicInfoRepo;

    public void init(String core) {
        ResourceBundle rb = ResourceBundle.getBundle("application");
        String urlString = rb.getString("spring.data.solr.host");
        this.solrServer = new HttpSolrServer(urlString + "/" + core);
    }

    public QueryResponse search(SolrQuery query) {
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 查询Paper
     *
     * @param page 页码
     * @param size 数量
     * @return List [Map <String, Object>]
     */
    public List<Map<String, Object>> getSolrPapers(Integer page, Integer size) {
        SolrQuery parameters = new SolrQuery();
        parameters.set("q", "*:*");
        parameters.set("sort", "id asc");
        parameters.set("fl", "*");
        parameters.setSort("id", SolrQuery.ORDER.asc);
        parameters.setStart(page * size);
        parameters.setRows(size);
        QueryResponse queryResponse = search(parameters);
        if (queryResponse == null) {
            return null;
        }
        SolrDocumentList results = queryResponse.getResults();

        List<Map<String, Object>> recordMapList = new ArrayList<>();
        for (SolrDocument result : results) {
            Map<String, Object> oneRecordMap = result.entrySet().stream().collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
            );
            recordMapList.add(oneRecordMap);
        }
        return recordMapList;
    }


    /**
     * 更新 paper_wanfang 的期刊权重
     *
     * @param paperList paper_wanfang List[Map]
     */
    public void updateSolrPaper(List<Map<String, Object>> paperList) {
        if (CollectionUtils.isEmpty(paperList)) {
            return;
        }
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
        for (Map<String, Object> paperMap : paperList) {
            solrInputDocumentList.add(getSolrInput(paperMap));
        }

        if (CollectionUtils.isEmpty(solrInputDocumentList)) {
            return;
        }

        try {
            solrServer.add(solrInputDocumentList);
            solrServer.commit();
            logger.info("Commit success!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SolrInputDocument getSolrInput(Map<String, Object> paperMap) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        String id = (String) paperMap.get("id");
        String journalNameCN = (String) paperMap.get("journal_cn_s");
        solrInputDocument.addField("id", id);
        solrInputDocument.addField("journal_weight_i",
                Collections.singletonMap("set", getJournalWeight(journalNameCN))
        );
        return solrInputDocument;
    }

    private int getJournalWeight(String journalNameCN) {
        if (StringUtils.isEmpty(journalNameCN)) {
            return 1;
        }

        String[] split = journalNameCN.split(" ");
        if (split.length == 0) {
            return 1;
        }
        String magazineName = split[0];
        MagazineBasicInfo magazineBasicInfo = magazineBasicInfoRepo.findFirstByJournalName(magazineName);
        if (magazineBasicInfo == null) {
            return 1;
        }
        return magazineBasicInfo.getJournalWeight();
    }
}
