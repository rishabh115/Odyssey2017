package dev.rism.odyssey2016.Models;

/**
 * Created by risha on 03-10-2016.
 */
public class EventModel {
    String name,url;
   public EventModel()
    {

    }
  public EventModel(String name,String url)
   {
       this.name=name;
       this.url=url;
   }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
