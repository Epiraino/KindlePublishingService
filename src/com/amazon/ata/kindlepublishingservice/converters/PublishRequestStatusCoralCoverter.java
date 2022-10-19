package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.coral.converter.CoralConverterUtil;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.BookRecommendation;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.List;

public class PublishRequestStatusCoralCoverter {
    private PublishRequestStatusCoralCoverter(){}


    public static List<PublishingStatusRecord> toCoral(List<com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem>
                                                           publishingRecordItems) {
        return CoralConverterUtil.convertList(publishingRecordItems, PublishRequestStatusCoralCoverter::toCoral);
    }
    public static PublishingStatusRecord toCoral(com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem
                                                     publishingStatusItem) {
        return PublishingStatusRecord.builder()
                .withStatusMessage(publishingStatusItem.getStatusMessage())
                .withStatus(publishingStatusItem.getStatus().toString())
                .withBookId(publishingStatusItem.getBookId())
                .build();
    }
}
