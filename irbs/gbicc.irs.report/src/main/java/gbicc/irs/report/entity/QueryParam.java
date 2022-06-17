package gbicc.irs.report.entity;

public class QueryParam {
    private String year;
    private String month;
    private String quarter;
    private String type;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String half;

    public String getHalf() {
        return half;
    }

    public void setHalf(String half) {
        this.half = half;
    }
}
