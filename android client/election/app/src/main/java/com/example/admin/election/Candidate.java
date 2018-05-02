package com.example.admin.election;

/**
 * Created by admin on 29.04.2018.
 */

public class Candidate {
    String name;
    int voteCount;
    public String ftlValue;
    public Candidate(String name, int voteCount) {
        this.name = name;
        this.voteCount = voteCount;
    }

    public String getName() {
        return name;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getFtlValue() {
        return ftlValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setFtlValue(String ftlValue) {
        this.ftlValue = ftlValue;
    }
}
