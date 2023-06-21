package com.example.bepcom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FingerprintModel {
    @SerializedName("file_number")
    @Expose
    private String file_number;

    ArrayList<Object> fingerprints = new ArrayList<>();
    ArrayList<Object> fingerprint_images = new ArrayList<>();


    // Getter Methods

    public String getFile_number() {
        return file_number;
    }

    // Setter Methods

    public void setFile_number(String file_number) {
        this.file_number = file_number;
    }
}

