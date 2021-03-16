package com.baro.models;

import java.net.URI;

public class Slide {
    private final int slideNumber;

    private URI video;

    private String text;


    public Slide(int slideNumber) {
        this.slideNumber = slideNumber;
    }
}
