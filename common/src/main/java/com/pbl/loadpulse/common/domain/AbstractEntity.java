package com.pbl.loadpulse.common.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @CreatedDate private Timestamp createdAt;

  @LastModifiedDate private Timestamp updatedAt;

  private Timestamp deletedAt;
}
