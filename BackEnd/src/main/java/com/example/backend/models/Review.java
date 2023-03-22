package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "review")
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String author;

    @JsonIgnore
    private Timestamp date;
    private String content;
    private int rate;

    @JsonProperty("date")
    public String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy, EE HH:mm").format(date);
    }

    public Review(String author, Timestamp date, String content, int rate) {
        this.author = author;
        this.date = date;
        this.content = content;
        this.rate = rate;
    }
}
