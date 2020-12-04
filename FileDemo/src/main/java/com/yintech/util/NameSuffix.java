package com.yintech.util;

public class NameSuffix {
    private String name;
    private String suffix;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public static NameSuffix parse(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        NameSuffix nameSuffix = new NameSuffix();
        if (name.contains(".")) {
            // hello.m
            int dotIndex = name.lastIndexOf(".");
            if (dotIndex != -1) {
                String suffix = name.substring(dotIndex+1); // m
                nameSuffix.setSuffix(suffix);
                nameSuffix.setName(name.substring(0, dotIndex));
            }
        } else {
            nameSuffix.setName(name);
        }
        return nameSuffix;
    }
}
