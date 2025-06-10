package com.example.demo.property.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProperty is a Querydsl query type for Property
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProperty extends EntityPathBase<Property> {

    private static final long serialVersionUID = 1084992320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProperty property = new QProperty("property");

    public final NumberPath<java.math.BigDecimal> acquisitionTax = createNumber("acquisitionTax", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> allRentPrice = createNumber("allRentPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> allWarrantPrice = createNumber("allWarrantPrice", java.math.BigDecimal.class);

    public final StringPath aptName = createString("aptName");

    public final StringPath area1 = createString("area1");

    public final StringPath area2 = createString("area2");

    public final StringPath articleFeatureDesc = createString("articleFeatureDesc");

    public final StringPath articleFeatureDescription = createString("articleFeatureDescription");

    public final StringPath articleName = createString("articleName");

    public final StringPath articleNo = createString("articleNo");

    public final StringPath bathroomCount = createString("bathroomCount");

    public final StringPath buildingName = createString("buildingName");

    public final StringPath cityName = createString("cityName");

    public final com.example.demo.review.domain.QComplex complex;

    public final StringPath correspondingFloorCount = createString("correspondingFloorCount");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath dealOrWarrantPrc = createString("dealOrWarrantPrc");

    public final NumberPath<java.math.BigDecimal> dealPrice = createNumber("dealPrice", java.math.BigDecimal.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath detailDescription = createString("detailDescription");

    public final StringPath direction = createString("direction");

    public final StringPath directionBaseTypeName = createString("directionBaseTypeName");

    public final StringPath divisionName = createString("divisionName");

    public final NumberPath<java.math.BigDecimal> eduTax = createNumber("eduTax", java.math.BigDecimal.class);

    public final StringPath entranceTypeName = createString("entranceTypeName");

    public final ListPath<String, StringPath> etcFacilities = this.<String, StringPath>createList("etcFacilities", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigDecimal> etcFeeAmount = createNumber("etcFeeAmount", java.math.BigDecimal.class);

    public final StringPath exposeStartYMD = createString("exposeStartYMD");

    public final StringPath exposureAddress = createString("exposureAddress");

    public final NumberPath<java.math.BigDecimal> financePrice = createNumber("financePrice", java.math.BigDecimal.class);

    public final StringPath floorInfo = createString("floorInfo");

    public final StringPath heatFuelTypeName = createString("heatFuelTypeName");

    public final StringPath heatMethodTypeName = createString("heatMethodTypeName");

    public final StringPath householdCount = createString("householdCount");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final ListPath<String, StringPath> lifeFacilities = this.<String, StringPath>createList("lifeFacilities", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath mainPurpsCdNm = createString("mainPurpsCdNm");

    public final StringPath moveInPossibleYmd = createString("moveInPossibleYmd");

    public final StringPath moveInTypeName = createString("moveInTypeName");

    public final StringPath parkingCount = createString("parkingCount");

    public final StringPath parkingCountPerHousehold = createString("parkingCountPerHousehold");

    public final StringPath parkingPossibleYN = createString("parkingPossibleYN");

    public final NumberPath<java.math.BigDecimal> priceBySpace = createNumber("priceBySpace", java.math.BigDecimal.class);

    public final StringPath principalUse = createString("principalUse");

    public final NumberPath<Long> propertyId = createNumber("propertyId", Long.class);

    public final StringPath realEstateTypeName = createString("realEstateTypeName");

    public final NumberPath<Realty> realty = createNumber("realty", Realty.class);

    public final NumberPath<java.math.BigDecimal> registTax = createNumber("registTax", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> rentPrice = createNumber("rentPrice", java.math.BigDecimal.class);

    public final StringPath roomCount = createString("roomCount");

    public final StringPath sameAddrMaxPrc = createString("sameAddrMaxPrc");

    public final StringPath sameAddrMinPrc = createString("sameAddrMinPrc");

    public final StringPath sectionName = createString("sectionName");

    public final ListPath<String, StringPath> securityFacilities = this.<String, StringPath>createList("securityFacilities", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigDecimal> specialTax = createNumber("specialTax", java.math.BigDecimal.class);

    public final ListPath<String, StringPath> tagList = this.<String, StringPath>createList("tagList", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath totalFloorCount = createString("totalFloorCount");

    public final StringPath tradeCompleteYN = createString("tradeCompleteYN");

    public final StringPath tradeTypeName = createString("tradeTypeName");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath useApproveYmd = createString("useApproveYmd");

    public final StringPath walkingTimeToNearSubway = createString("walkingTimeToNearSubway");

    public final NumberPath<java.math.BigDecimal> warrantPrice = createNumber("warrantPrice", java.math.BigDecimal.class);

    public QProperty(String variable) {
        this(Property.class, forVariable(variable), INITS);
    }

    public QProperty(Path<? extends Property> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProperty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProperty(PathMetadata metadata, PathInits inits) {
        this(Property.class, metadata, inits);
    }

    public QProperty(Class<? extends Property> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.complex = inits.isInitialized("complex") ? new com.example.demo.review.domain.QComplex(forProperty("complex"), inits.get("complex")) : null;
    }

}

