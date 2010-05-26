Wandledi
========

Wandledi is a little *Java* web framework still based on the Java Servlet 2.5 specification. It follows an alternative approach to the whole template business.
See below for more details.


Repository structure
--------------------

* **framework** - The actual Wandledi project
* **application** - An example web application using Wandledi

How it used to be
-----------------

There is this widely used approach where you have a controller,
which does the logic and fetches necessary data to hand it
over to a template file (jsp, erb, etc.).
So you always have two parts. The controller and the template.

Wandledi can do that, too.
This looks as follows.

**Controller**:

    import wandledi.java.Controller;

    public class HomeController extends Controller {
        public void index() {
            model.put("wb", session.get("user") != null);
            model.put("msg", "Hello World!");
        }
    }

**Template** (index.jsp):

    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
    <html>
      <body>
        <c:if test="${wb}">
          Welcome back and ...
        </c:if>
        <p>${msg}</p>
      </body>
    </html>

We all know this. But personally I don't like it.
I don't like "programming" with a cumbersome replacement (read EL and/or JSTL)
for the actual programming language I'm working with.
These ugly if-tags and other directives not only clutter what's really important
(the HTML), but also they mess it up with "programmer's stuff" which not only
confused HTML editing software like Dreamweaver but also your web designer.

The Wandledi approach
---------------------

So I'd rather like to do keep the HTML clean and express stuff like if and loops
in a real programming language. Now let's see how this is done in Wandledi.
First Wandledi introduces a third member to the party after throwing out
the template and replacing it with plain HTML*.

Magic. That is Spells. Spell is just a funny word for transformation
I came up with because transformation had to many syllables for my taste
and overall I have the unhealthy habit of using overly figurative names.

At the moment the class that uses these spells is called Pages.
Not very enchanting, but the namespace is pure chaos right now anyway.
The Pages class provides methods to the actual controller to transform
every HTML page.

So this leaves us with the three parts: Controller, Pages, XHTML file.

\**XHTML to be more precise since currently I'm simply using \*SAX\* to parse
the pages.*

**Controller**:

    import wandledi.java.PageController;

    public class HomeController extends PageController {
    
        HomePages pages = new HomePages();
        
        public void index() {
            boolean wb = session.get("user") != null;
            String msg = "Hello World!";
            
            pages.index(wb, msg);
        }
        
        @Override
        public Pages getPages() {
            return pages;
        }
    }
    
**Pages**:

    import wandledi.java.Pages;
    
    public class HomePages extends Pages {
    
        public void index(boolean wb, String msg) {
            if (wb) {
                get("body").insert("Welcome back and ...");
            }
            get("#msg").insert(msg);
        }
    }
    
**XHTML file**:

    <html>
      <body>
        <p id="msg">Message!</p>
      </body>
    </html>
    
Now this is a little more code. After all you have a whole class more.
But hey, at least the HTML file got a little shorter.
Moreover it now contains only plain HTML, which can actually be displayed by a browser.

Great, isn't it?
----------------

Well, I haven't found out yet, if and how great this is.
But as I continue this project I will try to build a medium sized
application with this approach and eventually I will see how it works out.

How is it coming along?
-----------------------

At the moment the repository is quite a mess with scattered code and
randomly named classes as I still haven't made a final decision
as to how to name everything.
Transformation, Spell, Wossname.
PageController, Grimoire, Wizard. Everything's possible.

So far the following Spells/Transformations are working:

* Invisibility - hides an element
* Clone - clones an element
* Insertion - inserts something\* into an element
* Replacement - replaces an element with something\* else
* Inclusion - inserts another XHTML file in place of an element
* AttributeTransformation - changes the attributes of an element

\**Something is either a String or more XHTML, which is generated with the
arch spell, which outputs valid XHTML.*

