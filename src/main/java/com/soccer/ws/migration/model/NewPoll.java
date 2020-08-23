package com.soccer.ws.migration.model;

import com.soccer.ws.data.PollStatusEnum;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by u0090265 on 29/05/16.
 */
@Entity
@Table(name = "polls")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class NewPoll<T> extends NewBaseClass {
    private String question;
    private DateTime startDate;
    private DateTime endDate;
    private PollStatusEnum status = PollStatusEnum.WAITING;

    public NewPoll() {
    }

    @NotNull
    @Column(name = "question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @NotNull
    @Column(name = "start_date")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    @NotNull
    @Column(name = "end_date")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @NotNull
    public PollStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PollStatusEnum status) {
        this.status = status;
    }

    @Transient
    public abstract void replaceVote(NewVote newVote);

    @Transient
    public abstract Set<? extends NewVote<T>> getVotes();

    @Transient
    public abstract Set<? extends NewOption<T>> getOptions();

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
