package com.medandev.sspl2;

public class DataRuang {
    private String id, ruang;

    public DataRuang() {
    }

    public DataRuang(String id, String ruang) {
        this.id = id;
        this.ruang = ruang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuang() {
        return ruang;
    }

    public void setRuang(String ruang) {
        this.ruang = ruang;
    }
}
