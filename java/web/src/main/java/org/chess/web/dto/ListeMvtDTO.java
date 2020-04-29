package org.chess.web.dto;

import java.util.List;

public class ListeMvtDTO {

    private String message;

    private List<String> deplacements;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDeplacements() {
        return deplacements;
    }

    public void setDeplacements(List<String> deplacements) {
        this.deplacements = deplacements;
    }
}
