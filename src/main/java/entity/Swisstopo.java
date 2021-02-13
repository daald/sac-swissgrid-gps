package entity;

import java.util.List;

public class Swisstopo {
  public List<Segment> segments;
  public String orig_url;
  public Author author;
  public Integer author_id;
  public List<AuthorMap> authors;
  public String availability;
  public Book book;

  public static class Book {
    public String author;
    public String edition;
    public Integer id;
    public String isbn;
    public String subtitle;
    public String title;
    public Integer year;
  }

  public static class Author {
    public String full_name;
    public Integer id;
    public String userid;
  }

  public static class AuthorMap {
    public Author author;
    public Integer author_id;
  }

  public static class Segment {
    public Boolean alternative;
    public String description;
    public Boolean download_end;
    public Boolean download_start;
    public Geom geom;
    public String title;
    public String discipline;
  }

  public static class Geom {
    public List<List<Double>> coordinates;
  }
}
