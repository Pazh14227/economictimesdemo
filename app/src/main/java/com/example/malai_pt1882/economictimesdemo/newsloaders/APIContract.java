package com.example.malai_pt1882.economictimesdemo.loaders;

final class APIContract {

    /**
     * Private constructor to ensure that no object is created for this class
     */
    private APIContract(){

    }

    /**
     * These are the object names or array names in JSON file reterived from newsapi.org
     */
    static final String STATUS_KEYWORD = "status";
    static final String TOTAL_RESULTS_KEYWORD = "totalResults";
    static final String ARTICLE_KEYWORD = "articles";
    static final String ARTICLE_SOURCE_KEYWORD = "source";
    static final String ARTICLE_SOURCE_ID_KEYWORD = "id";
    static final String ARTICLE_SOURCE_NAME_KEYWORD = "name";
    static final String ARTICLE_AUTHOR_KEYWORD = "author";
    static final String ARTICLE_TITLE_KEYWORD = "title";
    static final String ARTICLE_DESCRIPTION_KEYWORD = "description";
    static final String ARTICLE_URL_KEYWORD = "url";
    static final String ARTICLE_URL_TO_IMAGE_KEYWORD = "urlToImage";
    static final String ARTICLE_PUBLISHED_AT_KEYWORD = "publishedAt";

    /**
     * URL's for getting news data associated with each tab
     */
    static final String NEWS_URL = "https://newsapi.org/v2/top-headlines?category=general&country=in&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String QUICK_READS_URL = "https://newsapi.org/v2/top-headlines?category=business&country=in&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String MARKET_URL = "https://newsapi.org/v2/everything?q=market&sortBy=publishedAt&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String JOBS_URL = "https://newsapi.org/v2/everything?q=jobs&sortBy=publishedAt&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String INDIA_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String WORLD_URL = "https://newsapi.org/v2/top-headlines?category=general&apiKey=ab275994cb4b46e5964cadef4c633c82";
    static final String TECH_URL = "https://newsapi.org/v2/top-headlines?category=technology&country=in&apiKey=ab275994cb4b46e5964cadef4c633c82";
}