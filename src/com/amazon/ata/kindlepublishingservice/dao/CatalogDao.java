package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.converters.CatalogItemConverter;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;
    private KindlePublishingUtils kindlePublishingUtils = new KindlePublishingUtils();

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    public CatalogItemVersion removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
        book.setInactive(true);
        dynamoDbMapper.save(book);
        return book;

    }

    public CatalogItemVersion validateBookExists(String bookId){

        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;

    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleFormattedBook){
        CatalogItemVersion catalogItemVersion = new CatalogItemVersion();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!createorupdate reached!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
        if (kindleFormattedBook.getBookId() == null){
            String newBookId = kindlePublishingUtils.generateBookId();
            catalogItemVersion.setBookId(newBookId);
            System.out.println("THIS IS THE BOOK ID  " + catalogItemVersion.getBookId());
            System.out.println("THIS IS THE BOOK ID  " + newBookId);
            catalogItemVersion.setAuthor(kindleFormattedBook.getAuthor());
            catalogItemVersion.setGenre(kindleFormattedBook.getGenre());
            catalogItemVersion.setText(kindleFormattedBook.getText());
            catalogItemVersion.setAuthor(kindleFormattedBook.getAuthor());
            catalogItemVersion.setTitle(kindleFormattedBook.getTitle());
            catalogItemVersion.setVersion(1);

            dynamoDbMapper.save(catalogItemVersion);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!saved!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
            return catalogItemVersion;
        }
        validateBookExists(kindleFormattedBook.getBookId());
        CatalogItemVersion existingBook = getLatestVersionOfBook(kindleFormattedBook.getBookId());
        CatalogItemVersion newBook = getBookFromCatalog(kindleFormattedBook.getBookId());
        int version = existingBook.getVersion();


        int newVersion = version + 1;
        existingBook.setInactive(true);
        newBook.setTitle(kindleFormattedBook.getTitle());
        newBook.setAuthor(kindleFormattedBook.getAuthor());
        newBook.setGenre(kindleFormattedBook.getGenre());
        newBook.setText(kindleFormattedBook.getText());
        newBook.setAuthor(kindleFormattedBook.getAuthor());
        newBook.setBookId(kindleFormattedBook.getBookId());
        newBook.setVersion(newVersion);
        System.out.println("VERSION NUMBER " + newVersion + "  " + existingBook.getVersion());
        dynamoDbMapper.save(existingBook);
        dynamoDbMapper.save(newBook);
        //finally
        return newBook;
    }


    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
}
