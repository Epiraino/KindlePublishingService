package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Singleton
public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequests = new LinkedList<>();

    public BookPublishRequestManager() {}

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest){
        bookPublishRequests.add(bookPublishRequest);
        System.out.println("request added " + bookPublishRequest.toString());
    }

    public BookPublishRequest getBookPublishRequestToProcess(){
        if(bookPublishRequests.isEmpty()){
            return null;
        }
        return bookPublishRequests.remove();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookPublishRequestManager that = (BookPublishRequestManager) o;
        return Objects.equals(bookPublishRequests, that.bookPublishRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookPublishRequests);
    }
}
