package com.bpms.config;

public final class SessionKeys {
    private SessionKeys() {}

    public static final String RESIDENT_ID = "residentId";
    public static final String PARCELMAN_ID = "parcelmanId";

    // in-progress admin pickup wizard state
    public static final String PICKUP_STEP = "pickup.step";
    public static final String PICKUP_PARCEL_ID = "pickup.parcelId";
    public static final String PICKUP_VERIFIED = "pickup.verified";
    public static final String PICKUP_LAST_RECORD_ID = "pickup.lastRecordId";
}
