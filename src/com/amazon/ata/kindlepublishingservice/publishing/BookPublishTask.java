package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import dagger.Provides;


import javax.inject.Inject;
import javax.inject.Singleton;



public class BookPublishTask implements Runnable {

    private PublishingStatusDao publishingStatusDao;
    private BookPublishRequestManager bookPublishRequestManager;
    private CatalogDao catalogDao;
    private KindleFormatConverter kindleFormatConverter;


    @Inject
    public BookPublishTask(PublishingStatusDao publishingStatusDao, BookPublishRequestManager bookPublishRequestManager,
                           CatalogDao catalogDao){
        this.publishingStatusDao = publishingStatusDao;
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.catalogDao = catalogDao;

    }

    @Override
    public void run() {
        System.out.println("i am Running");

        while(true){
            BookPublishRequest bookPublishRequest= bookPublishRequestManager.getBookPublishRequestToProcess();

            if (bookPublishRequest == null){
                System.out.println("I am Stopping");
                return;
            }

            System.out.println("Book Request" + bookPublishRequest.toString());

            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());

            System.out.println("completed INPROGRESS");

           KindleFormattedBook kindleFormattedBook = kindleFormatConverter.format(bookPublishRequest);

           CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(kindleFormattedBook);

           publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                   PublishingRecordStatus.SUCCESSFUL, catalogItemVersion.getBookId());




            return;
        }
//        if (bookPublishRequestManager.getBookPublishRequestToProcess() == null){
//            System.out.println("I am Stopping");
//            return;
//        }
//
//        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
//        publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS,
//                request.getBookId());


    }
}
