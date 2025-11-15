package com.felissedano.dailyreflect.journaling;

import com.felissedano.dailyreflect.profile.Profile;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "content", nullable = true)
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(
            name = "journals_profile",
            joinColumns = @JoinColumn(name = "journal_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false),
            indexes = @Index(columnList = "profile_id"))
    private Profile profile;

    public Journal() {}

    public Journal(LocalDate date, String content, List<String> tags, Profile profile) {
        this.date = date;

        this.content = content;
        this.tags = tags;
        this.profile = profile;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Journal{" + "id=" + id + ", date=" + date + ", content='" + content + "'" + ", tags=" + tags + "}";
    }
}
