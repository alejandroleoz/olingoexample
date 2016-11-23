package mago.olingo.example;

import mago.olingo.example.db.AbstractRepository;
import mago.olingo.example.db.MySQLConnection;
import mago.olingo.example.model.Author;
import mago.olingo.example.model.Book;

import java.util.List;

/**
 * Created by aleoz on 11/22/16.
 */
public class App {

    public static void main(String[] args) {
        AbstractRepository<Book> booksRepo = new AbstractRepository<Book>(MySQLConnection.getInstance(), Book.class) {
            public List<Book> getAll() {
                return this.connection.getEntityManager().createQuery("select b from Book b").getResultList();
            }
        };

        AbstractRepository<Author> authorRepo = new AbstractRepository<Author>(MySQLConnection.getInstance(), Author.class) {
            public List<Author> getAll() {
                return this.connection.getEntityManager().createQuery("select a from Author a").getResultList();
            }
        };

        Author author = new Author();
        author.setName("Alejandro");
        authorRepo.save(author);

        Book book = new Book();
        book.setTitle("LALA");
        book.setDescription("abcdefghij");
        book.setAuthor(author);
        booksRepo.save(book);

    }
}
