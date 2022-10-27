package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.*;


import dagger.Module;
import dagger.Provides;
import org.springframework.cglib.proxy.NoOp;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService, BookPublishTask bookPublishTask) {
        return new BookPublisher(scheduledExecutorService, bookPublishTask);
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }

    @Provides
    @Singleton
    public BookPublishTask provideBookPublishTask(PublishingStatusDao publishingStatusDao, BookPublishRequestManager bookPublishRequestManager, CatalogDao catalogDao){
        return new BookPublishTask(publishingStatusDao, bookPublishRequestManager, catalogDao);
    }
    @Provides
    @Singleton
    public BookPublishRequestManager providesBookPublishRequestManager(){
        return new BookPublishRequestManager();
    }


//    @Provides
//    @Singleton
//    public BookPublishRequest provideBookPublishRequest(){
//        return new BookPublishRequest();
//    }

}
