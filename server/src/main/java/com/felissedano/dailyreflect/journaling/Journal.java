package com.felissedano.dailyreflect.journaling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.felissedano.dailyreflect.profile.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "tags", nullable = true)
    private ArrayList<String> tags;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(name = "profile_journals",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "journal_id", referencedColumnName = "id"),
            indexes = @Index(columnList = "profile_id")
    )
    private Profile profile;

    public Journal() {
    }

    public Journal(Date date, String content, ArrayList<String> tags, Profile profile) {
        this.date = date;

        this.content = content;
        this.tags = tags;
        this.profile = profile;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", date=" + date +
                ", content='" + content + "'" +
                ", tags=" + tags +
                "}";
    }
}
