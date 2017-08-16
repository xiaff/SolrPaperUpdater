package com.kejso.paper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 期刊的基本信息表
 * Created by Lu Chenwei on 2017/8/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "magazine_basic_info")
public class MagazineBasicInfo {
    @Id
    private Integer id;
    private String journalName;
    private String classify;
    private Integer journalWeight;
}
