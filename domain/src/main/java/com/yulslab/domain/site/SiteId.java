package com.yulslab.domain.site;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class SiteId implements Serializable {
    private String companyId;
    private String siteId;
}
