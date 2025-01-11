package com.example.serradilla_alvaroimdbapp.models;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("title")
        private Title title;

        public Title getTitle() {
            return title;
        }
    }

    public static class Title {
        @SerializedName("id")
        private String id;

        @SerializedName("titleText")
        private TitleText titleText;

        @SerializedName("releaseYear")
        private ReleaseYear releaseYear;

        @SerializedName("releaseDate")
        private ReleaseDate releaseDate;

        @SerializedName("primaryImage")
        private PrimaryImage primaryImage;

        @SerializedName("ratingsSummary")
        private RatingsSummary ratingsSummary;

        @SerializedName("plot")
        private Plot plot;

        public String getId() {
            return id;
        }

        public TitleText getTitleText() {
            return titleText;
        }

        public ReleaseYear getReleaseYear() {
            return releaseYear;
        }

        public ReleaseDate getReleaseDate() {
            return releaseDate;
        }

        public PrimaryImage getPrimaryImage() {
            return primaryImage;
        }

        public RatingsSummary getRatingsSummary() {
            return ratingsSummary;
        }

        public Plot getPlot() {
            return plot;
        }
    }

    public static class TitleText {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }

    public static class ReleaseYear {
        @SerializedName("year")
        private int year;

        public int getYear() {
            return year;
        }
    }

    public static class ReleaseDate {
        @SerializedName("day")
        private int day;

        @SerializedName("month")
        private int month;

        @SerializedName("year")
        private int year;

        public String getFormattedDate() {
            return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
        }
    }

    public static class PrimaryImage {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public static class RatingsSummary {
        @SerializedName("aggregateRating")
        private float aggregateRating;

        public float getAggregateRating() {
            return aggregateRating;
        }
    }

    public static class Plot {
        @SerializedName("plotText")
        private PlotText plotText;

        public PlotText getPlotText() {
            return plotText;
        }
    }

    public static class PlotText {
        @SerializedName("plainText")
        private String plainText;

        public String getPlainText() {
            return plainText;
        }
    }
}




