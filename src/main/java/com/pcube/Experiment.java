package com.pcube;

public class Experiment {
	
	private final long id;
    private final String content;

    public Experiment(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
