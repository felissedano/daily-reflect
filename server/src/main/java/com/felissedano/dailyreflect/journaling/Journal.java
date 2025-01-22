package com.felissedano.dailyreflect.journaling;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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

    public Journal() {
    }

    public Journal(Date date, String content, ArrayList<String> tags) {
        this.date = date;
        this.content = content;
        this.tags = tags;
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

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                '}';
    }
}
