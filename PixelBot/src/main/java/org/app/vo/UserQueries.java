package org.app.vo;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "user_queries")
public class UserQueries {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String query;
    @Column
    private Date date_initiated;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String s3_filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getDate_initiated() {
        return date_initiated;
    }

    public void setDate_initiated(Date date_initiated) {
        this.date_initiated = date_initiated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getS3_filename() {
        return s3_filename;
    }

    public void setS3_filename(String s3_filename) {
        this.s3_filename = s3_filename;
    }
}
