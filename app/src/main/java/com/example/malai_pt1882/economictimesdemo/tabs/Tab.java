package com.example.malai_pt1882.economictimesdemo.tabs;

public enum Tab {
    NEWS("News"), QUICK_READS("Quick Reads"), MARKET("Market"), JOBS("Jobs"), INDIA("India"), WORLD("World"), TECH("Tech");

    private final String title;

    Tab(String title) {
        this.title = title;
    }

    public String getTabTitle() {
        return title;
    }

}
