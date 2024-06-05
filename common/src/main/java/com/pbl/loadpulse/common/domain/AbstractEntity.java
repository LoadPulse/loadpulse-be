package com.pbl.loadpulse.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @CreatedDate private Timestamp createdAt;

  @LastModifiedDate private Timestamp updatedAt;

  private Timestamp deletedAt;
}
