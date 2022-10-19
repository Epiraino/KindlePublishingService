package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequests;

    @Inject
    public BookPublishRequestManager(Queue<BookPublishRequest> bookPublishRequests) {
        this.bookPublishRequests = bookPublishRequests;
    }

    public Queue<BookPublishRequest> getBookPublishRequests() {
        return bookPublishRequests;
    }

    public void setBookPublishRequests(Queue<BookPublishRequest> bookPublishRequests) {
        this.bookPublishRequests = bookPublishRequests;
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest){
        bookPublishRequests.add(bookPublishRequest);
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
