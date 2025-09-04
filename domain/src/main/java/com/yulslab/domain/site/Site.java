package com.yulslab.domain.site;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "site")
@Getter
@Setter
public class Site {
    @EmbeddedId
    private SiteId id;
    private String siteName;
    private char useYn;
}
