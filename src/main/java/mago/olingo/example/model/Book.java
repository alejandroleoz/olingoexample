package mago.olingo.example.model;

import org.apache.olingo.odata2.api.annotation.edm.*;

/**
 * Created by aleoz on 11/22/16.
 */

@EdmEntityType
@EdmEntitySet
public class Book {

    @EdmKey
    @EdmProperty
    private Long id;

    @EdmProperty
    private String name;

    @EdmProperty
    private String description;

    @EdmNavigationProperty(
            name = "author",
            toType = Author.class,
            toMultiplicity = EdmNavigationProperty.Multiplicity.ZERO_OR_ONE)
    private Author author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
