package com.dynamo.cr.web2.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ProjectPlace extends DefoldPlace {

    private String id;
    public ProjectPlace(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return "Project Info";
    }

    @Prefix("project")
    public static class Tokenizer implements PlaceTokenizer<ProjectPlace> {
        @Override
        public String getToken(ProjectPlace place) {
            return place.getId();
        }

        @Override
        public ProjectPlace getPlace(String token) {
            return new ProjectPlace(token);
        }
    }
}