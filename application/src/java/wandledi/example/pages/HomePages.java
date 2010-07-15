package wandledi.example.pages;

import java.util.Collection;
import wandledi.example.models.BlogEntry;
import wandledi.java.html.Pages;
import wandledi.java.html.Plan;
import wandledi.java.html.SelectableElement;

/**
 *
 * @author Markus Kahl
 */
public class HomePages extends Pages {

    public void index(String time, String msg, boolean loggedIn, Collection<BlogEntry> entries) {

        get("#time").replace(true, time);
        if (msg != null) {
            get("#right").insert(msg);
        }
        if (loggedIn) {
            select("a", "href", "login").setAttribute("href", "post");
            get("button").replace(true, "Post Entry");
        }
        if (entries.isEmpty()) {
            get(".entry").setAttribute("id", "last");
            get(".heading").replace(true, "No Entries yet");
            get(".text").replace(true, "There are no entries yet. Why don't you start writing one?");
        } else {
            get(".entry").foreachIn(entries).apply(new Plan<BlogEntry>() {
                public void execute(SelectableElement element, BlogEntry entry) {
                    if (last()) {
                        element.setAttribute("id", "last");
                    }
                    element.get(".heading").replace(true, entry.getTitle());
                    element.get(".text").replace(true, entry.getContent());
                }
            });
        }
    }

    public void login() {

        setFile("home/index.xhtml");
        get(".heading").includeFile("/home/login.xhtml");
        get("p").hide();
    }

    public void post() {
        
    }
}