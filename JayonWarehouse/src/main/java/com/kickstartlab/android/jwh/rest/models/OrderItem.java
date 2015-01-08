package com.kickstartlab.android.jwh.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 12/3/14.
 */
public class OrderItem extends SugarRecord<OrderItem> {

    private Long extId;
    private String created;
    private String ordertime;
    private String pickuptime;
    private String buyerdeliverytime;
    private String buyerdeliveryslot;
    private String buyerdeliveryzone;
    private String buyerdeliverycity;
    private String assigntime;
    private String deliverytime;
    private String assignmentDate;
    private String assignmentTimeslot;
    private String assignmentZone;
    private String assignmentCity;
    private long assignmentSeq;
    private String deliveryId;
    private long deliveryCost;
    private long codCost;
    private long width;
    private long height;
    private long length;
    private long weight;
    private long actualWeight;
    private String deliveryType;
    private String currency;
    private long totalPrice;
    private long fixedDiscount;
    private long totalDiscount;
    private long totalTax;
    private long chargeableAmount;
    private String deliveryBearer;
    private String codBearer;
    private String codMethod;
    private String ccodMethod;
    private long applicationId;
    private String applicationKey;
    private String buyerId;
    private String merchantId;
    private String merchantTransId;
    private long toscan;
    private String pickupStatus;
    private String warehouseStatus;
    private String warehouseIn;
    private String warehouseOut;
    private String courierId;
    private String deviceId;
    private String pickupDevId;
    private String pickupPerson;
    private String buyerName;
    private String email;
    private String recipientName;
    private String shippingAddress;
    private String shippingZip;
    private String directions;
    private double dirLat;
    private double dirLon;
    private String phone;
    private String mobile1;
    private String mobile2;
    private String status;
    private String laststatus;
    private long pendingCount;
    private String changeActor;
    private String puchangeActor;
    private String whchangeActor;
    private String actorHistory;
    private String deliveryNote;
    private String warehouseNote;
    private String pickupNote;
    private String recieverName;
    private String recieverPicture;
    private String picAddress;
    private String pic1;
    private String pic2;
    private String pic3;
    private String undersign;
    private double latitude;
    private double longitude;
    private double photolatitude;
    private double photolongitude;
    private String rescheduleRef;
    private String revokeRef;
    private long reattemp;
    private long showMerchant;
    private long showShop;
    private long isPickup;
    private long isImport;
    private long isApi;
    private String merchantName;
    private String deviceName;
    private String courierName;
    private String orderSrc;

    public OrderItem(){

    }

    public String toString() {
        return deliveryId;
    }

    /**
     *
     * @return
     * The extId
     */
    public Long getExtId() {
        return extId;
    }

    /**
     *
     * @param extId
     * The extId
     */
    public void setExtId(Long extId) {
        this.extId = extId;
    }

    /**
     *
     * @return
     * The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     * The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     * The ordertime
     */
    public String getOrdertime() {
        return ordertime;
    }

    /**
     *
     * @param ordertime
     * The ordertime
     */
    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    /**
     *
     * @return
     * The pickuptime
     */
    public String getPickuptime() {
        return pickuptime;
    }

    /**
     *
     * @param pickuptime
     * The pickuptime
     */
    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    /**
     *
     * @return
     * The buyerdeliverytime
     */
    public String getBuyerdeliverytime() {
        return buyerdeliverytime;
    }

    /**
     *
     * @param buyerdeliverytime
     * The buyerdeliverytime
     */
    public void setBuyerdeliverytime(String buyerdeliverytime) {
        this.buyerdeliverytime = buyerdeliverytime;
    }

    /**
     *
     * @return
     * The buyerdeliveryslot
     */
    public String getBuyerdeliveryslot() {
        return buyerdeliveryslot;
    }

    /**
     *
     * @param buyerdeliveryslot
     * The buyerdeliveryslot
     */
    public void setBuyerdeliveryslot(String buyerdeliveryslot) {
        this.buyerdeliveryslot = buyerdeliveryslot;
    }

    /**
     *
     * @return
     * The buyerdeliveryzone
     */
    public String getBuyerdeliveryzone() {
        return buyerdeliveryzone;
    }

    /**
     *
     * @param buyerdeliveryzone
     * The buyerdeliveryzone
     */
    public void setBuyerdeliveryzone(String buyerdeliveryzone) {
        this.buyerdeliveryzone = buyerdeliveryzone;
    }

    /**
     *
     * @return
     * The buyerdeliverycity
     */
    public String getBuyerdeliverycity() {
        return buyerdeliverycity;
    }

    /**
     *
     * @param buyerdeliverycity
     * The buyerdeliverycity
     */
    public void setBuyerdeliverycity(String buyerdeliverycity) {
        this.buyerdeliverycity = buyerdeliverycity;
    }

    /**
     *
     * @return
     * The assigntime
     */
    public String getAssigntime() {
        return assigntime;
    }

    /**
     *
     * @param assigntime
     * The assigntime
     */
    public void setAssigntime(String assigntime) {
        this.assigntime = assigntime;
    }

    /**
     *
     * @return
     * The deliverytime
     */
    public String getDeliverytime() {
        return deliverytime;
    }

    /**
     *
     * @param deliverytime
     * The deliverytime
     */
    public void setDeliverytime(String deliverytime) {
        this.deliverytime = deliverytime;
    }

    /**
     *
     * @return
     * The assignmentDate
     */
    public String getAssignmentDate() {
        return assignmentDate;
    }

    /**
     *
     * @param assignmentDate
     * The assignmentDate
     */
    public void setAssignmentDate(String assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    /**
     *
     * @return
     * The assignmentTimeslot
     */
    public String getAssignmentTimeslot() {
        return assignmentTimeslot;
    }

    /**
     *
     * @param assignmentTimeslot
     * The assignmentTimeslot
     */
    public void setAssignmentTimeslot(String assignmentTimeslot) {
        this.assignmentTimeslot = assignmentTimeslot;
    }

    /**
     *
     * @return
     * The assignmentZone
     */
    public String getAssignmentZone() {
        return assignmentZone;
    }

    /**
     *
     * @param assignmentZone
     * The assignmentZone
     */
    public void setAssignmentZone(String assignmentZone) {
        this.assignmentZone = assignmentZone;
    }

    /**
     *
     * @return
     * The assignmentCity
     */
    public String getAssignmentCity() {
        return assignmentCity;
    }

    /**
     *
     * @param assignmentCity
     * The assignmentCity
     */
    public void setAssignmentCity(String assignmentCity) {
        this.assignmentCity = assignmentCity;
    }

    /**
     *
     * @return
     * The assignmentSeq
     */
    public long getAssignmentSeq() {
        return assignmentSeq;
    }

    /**
     *
     * @param assignmentSeq
     * The assignmentSeq
     */
    public void setAssignmentSeq(long assignmentSeq) {
        this.assignmentSeq = assignmentSeq;
    }

    /**
     *
     * @return
     * The deliveryId
     */
    public String getDeliveryId() {
        return deliveryId;
    }

    /**
     *
     * @param deliveryId
     * The deliveryId
     */
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     *
     * @return
     * The deliveryCost
     */
    public long getDeliveryCost() {
        return deliveryCost;
    }

    /**
     *
     * @param deliveryCost
     * The deliveryCost
     */
    public void setDeliveryCost(long deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    /**
     *
     * @return
     * The codCost
     */
    public long getCodCost() {
        return codCost;
    }

    /**
     *
     * @param codCost
     * The codCost
     */
    public void setCodCost(long codCost) {
        this.codCost = codCost;
    }

    /**
     *
     * @return
     * The width
     */
    public long getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(long width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The height
     */
    public long getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(long height) {
        this.height = height;
    }

    /**
     *
     * @return
     * The length
     */
    public long getLength() {
        return length;
    }

    /**
     *
     * @param length
     * The length
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     *
     * @return
     * The weight
     */
    public long getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     * The weight
     */
    public void setWeight(long weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     * The actualWeight
     */
    public long getActualWeight() {
        return actualWeight;
    }

    /**
     *
     * @param actualWeight
     * The actualWeight
     */
    public void setActualWeight(long actualWeight) {
        this.actualWeight = actualWeight;
    }

    /**
     *
     * @return
     * The deliveryType
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     *
     * @param deliveryType
     * The deliveryType
     */
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     *
     * @return
     * The totalPrice
     */
    public long getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     * The totalPrice
     */
    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     *
     * @return
     * The fixedDiscount
     */
    public long getFixedDiscount() {
        return fixedDiscount;
    }

    /**
     *
     * @param fixedDiscount
     * The fixedDiscount
     */
    public void setFixedDiscount(long fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
    }

    /**
     *
     * @return
     * The totalDiscount
     */
    public long getTotalDiscount() {
        return totalDiscount;
    }

    /**
     *
     * @param totalDiscount
     * The totalDiscount
     */
    public void setTotalDiscount(long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    /**
     *
     * @return
     * The totalTax
     */
    public long getTotalTax() {
        return totalTax;
    }

    /**
     *
     * @param totalTax
     * The totalTax
     */
    public void setTotalTax(long totalTax) {
        this.totalTax = totalTax;
    }

    /**
     *
     * @return
     * The chargeableAmount
     */
    public long getChargeableAmount() {
        return chargeableAmount;
    }

    /**
     *
     * @param chargeableAmount
     * The chargeableAmount
     */
    public void setChargeableAmount(long chargeableAmount) {
        this.chargeableAmount = chargeableAmount;
    }

    /**
     *
     * @return
     * The deliveryBearer
     */
    public String getDeliveryBearer() {
        return deliveryBearer;
    }

    /**
     *
     * @param deliveryBearer
     * The deliveryBearer
     */
    public void setDeliveryBearer(String deliveryBearer) {
        this.deliveryBearer = deliveryBearer;
    }

    /**
     *
     * @return
     * The codBearer
     */
    public String getCodBearer() {
        return codBearer;
    }

    /**
     *
     * @param codBearer
     * The codBearer
     */
    public void setCodBearer(String codBearer) {
        this.codBearer = codBearer;
    }

    /**
     *
     * @return
     * The codMethod
     */
    public String getCodMethod() {
        return codMethod;
    }

    /**
     *
     * @param codMethod
     * The codMethod
     */
    public void setCodMethod(String codMethod) {
        this.codMethod = codMethod;
    }

    /**
     *
     * @return
     * The ccodMethod
     */
    public String getCcodMethod() {
        return ccodMethod;
    }

    /**
     *
     * @param ccodMethod
     * The ccodMethod
     */
    public void setCcodMethod(String ccodMethod) {
        this.ccodMethod = ccodMethod;
    }

    /**
     *
     * @return
     * The applicationId
     */
    public long getApplicationId() {
        return applicationId;
    }

    /**
     *
     * @param applicationId
     * The applicationId
     */
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     *
     * @return
     * The applicationKey
     */
    public String getApplicationKey() {
        return applicationKey;
    }

    /**
     *
     * @param applicationKey
     * The applicationKey
     */
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    /**
     *
     * @return
     * The buyerId
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     *
     * @param buyerId
     * The buyerId
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    /**
     *
     * @return
     * The merchantId
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     *
     * @param merchantId
     * The merchantId
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     *
     * @return
     * The merchantTransId
     */
    public String getMerchantTransId() {
        return merchantTransId;
    }

    /**
     *
     * @param merchantTransId
     * The merchantTransId
     */
    public void setMerchantTransId(String merchantTransId) {
        this.merchantTransId = merchantTransId;
    }

    /**
     *
     * @return
     * The toscan
     */
    public long getToscan() {
        return toscan;
    }

    /**
     *
     * @param toscan
     * The toscan
     */
    public void setToscan(long toscan) {
        this.toscan = toscan;
    }

    /**
     *
     * @return
     * The pickupStatus
     */
    public String getPickupStatus() {
        return pickupStatus;
    }

    /**
     *
     * @param pickupStatus
     * The pickupStatus
     */
    public void setPickupStatus(String pickupStatus) {
        this.pickupStatus = pickupStatus;
    }

    /**
     *
     * @return
     * The warehouseStatus
     */
    public String getWarehouseStatus() {
        return warehouseStatus;
    }

    /**
     *
     * @param warehouseStatus
     * The warehouseStatus
     */
    public void setWarehouseStatus(String warehouseStatus) {
        this.warehouseStatus = warehouseStatus;
    }

    /**
     *
     * @return
     * The warehouseIn
     */
    public String getWarehouseIn() {
        return warehouseIn;
    }

    /**
     *
     * @param warehouseIn
     * The warehouseIn
     */
    public void setWarehouseIn(String warehouseIn) {
        this.warehouseIn = warehouseIn;
    }

    /**
     *
     * @return
     * The warehouseOut
     */
    public String getWarehouseOut() {
        return warehouseOut;
    }

    /**
     *
     * @param warehouseOut
     * The warehouseOut
     */
    public void setWarehouseOut(String warehouseOut) {
        this.warehouseOut = warehouseOut;
    }

    /**
     *
     * @return
     * The courierId
     */
    public String getCourierId() {
        return courierId;
    }

    /**
     *
     * @param courierId
     * The courierId
     */
    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    /**
     *
     * @return
     * The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     * The deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     * The pickupDevId
     */
    public String getPickupDevId() {
        return pickupDevId;
    }

    /**
     *
     * @param pickupDevId
     * The pickupDevId
     */
    public void setPickupDevId(String pickupDevId) {
        this.pickupDevId = pickupDevId;
    }

    /**
     *
     * @return
     * The pickupPerson
     */
    public String getPickupPerson() {
        return pickupPerson;
    }

    /**
     *
     * @param pickupPerson
     * The pickupPerson
     */
    public void setPickupPerson(String pickupPerson) {
        this.pickupPerson = pickupPerson;
    }

    /**
     *
     * @return
     * The buyerName
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     *
     * @param buyerName
     * The buyerName
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The recipientName
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     *
     * @param recipientName
     * The recipientName
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    /**
     *
     * @return
     * The shippingAddress
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     *
     * @param shippingAddress
     * The shippingAddress
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     *
     * @return
     * The shippingZip
     */
    public String getShippingZip() {
        return shippingZip;
    }

    /**
     *
     * @param shippingZip
     * The shippingZip
     */
    public void setShippingZip(String shippingZip) {
        this.shippingZip = shippingZip;
    }

    /**
     *
     * @return
     * The directions
     */
    public String getDirections() {
        return directions;
    }

    /**
     *
     * @param directions
     * The directions
     */
    public void setDirections(String directions) {
        this.directions = directions;
    }

    /**
     *
     * @return
     * The dirLat
     */
    public double getDirLat() {
        return dirLat;
    }

    /**
     *
     * @param dirLat
     * The dirLat
     */
    public void setDirLat(double dirLat) {
        this.dirLat = dirLat;
    }

    /**
     *
     * @return
     * The dirLon
     */
    public double getDirLon() {
        return dirLon;
    }

    /**
     *
     * @param dirLon
     * The dirLon
     */
    public void setDirLon(double dirLon) {
        this.dirLon = dirLon;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     * The mobile1
     */
    public String getMobile1() {
        return mobile1;
    }

    /**
     *
     * @param mobile1
     * The mobile1
     */
    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    /**
     *
     * @return
     * The mobile2
     */
    public String getMobile2() {
        return mobile2;
    }

    /**
     *
     * @param mobile2
     * The mobile2
     */
    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The laststatus
     */
    public String getLaststatus() {
        return laststatus;
    }

    /**
     *
     * @param laststatus
     * The laststatus
     */
    public void setLaststatus(String laststatus) {
        this.laststatus = laststatus;
    }

    /**
     *
     * @return
     * The pendingCount
     */
    public long getPendingCount() {
        return pendingCount;
    }

    /**
     *
     * @param pendingCount
     * The pendingCount
     */
    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    /**
     *
     * @return
     * The changeActor
     */
    public String getChangeActor() {
        return changeActor;
    }

    /**
     *
     * @param changeActor
     * The changeActor
     */
    public void setChangeActor(String changeActor) {
        this.changeActor = changeActor;
    }

    /**
     *
     * @return
     * The puchangeActor
     */
    public String getPuchangeActor() {
        return puchangeActor;
    }

    /**
     *
     * @param puchangeActor
     * The puchangeActor
     */
    public void setPuchangeActor(String puchangeActor) {
        this.puchangeActor = puchangeActor;
    }

    /**
     *
     * @return
     * The whchangeActor
     */
    public String getWhchangeActor() {
        return whchangeActor;
    }

    /**
     *
     * @param whchangeActor
     * The whchangeActor
     */
    public void setWhchangeActor(String whchangeActor) {
        this.whchangeActor = whchangeActor;
    }

    /**
     *
     * @return
     * The actorHistory
     */
    public String getActorHistory() {
        return actorHistory;
    }

    /**
     *
     * @param actorHistory
     * The actorHistory
     */
    public void setActorHistory(String actorHistory) {
        this.actorHistory = actorHistory;
    }

    /**
     *
     * @return
     * The deliveryNote
     */
    public String getDeliveryNote() {
        return deliveryNote;
    }

    /**
     *
     * @param deliveryNote
     * The deliveryNote
     */
    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    /**
     *
     * @return
     * The warehouseNote
     */
    public String getWarehouseNote() {
        return warehouseNote;
    }

    /**
     *
     * @param warehouseNote
     * The warehouseNote
     */
    public void setWarehouseNote(String warehouseNote) {
        this.warehouseNote = warehouseNote;
    }

    /**
     *
     * @return
     * The pickupNote
     */
    public String getPickupNote() {
        return pickupNote;
    }

    /**
     *
     * @param pickupNote
     * The pickupNote
     */
    public void setPickupNote(String pickupNote) {
        this.pickupNote = pickupNote;
    }

    /**
     *
     * @return
     * The recieverName
     */
    public String getRecieverName() {
        return recieverName;
    }

    /**
     *
     * @param recieverName
     * The recieverName
     */
    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    /**
     *
     * @return
     * The recieverPicture
     */
    public String getRecieverPicture() {
        return recieverPicture;
    }

    /**
     *
     * @param recieverPicture
     * The recieverPicture
     */
    public void setRecieverPicture(String recieverPicture) {
        this.recieverPicture = recieverPicture;
    }

    /**
     *
     * @return
     * The picAddress
     */
    public String getPicAddress() {
        return picAddress;
    }

    /**
     *
     * @param picAddress
     * The picAddress
     */
    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    /**
     *
     * @return
     * The pic1
     */
    public String getPic1() {
        return pic1;
    }

    /**
     *
     * @param pic1
     * The pic1
     */
    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    /**
     *
     * @return
     * The pic2
     */
    public String getPic2() {
        return pic2;
    }

    /**
     *
     * @param pic2
     * The pic2
     */
    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    /**
     *
     * @return
     * The pic3
     */
    public String getPic3() {
        return pic3;
    }

    /**
     *
     * @param pic3
     * The pic3
     */
    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    /**
     *
     * @return
     * The undersign
     */
    public String getUndersign() {
        return undersign;
    }

    /**
     *
     * @param undersign
     * The undersign
     */
    public void setUndersign(String undersign) {
        this.undersign = undersign;
    }

    /**
     *
     * @return
     * The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The photolatitude
     */
    public double getPhotolatitude() {
        return photolatitude;
    }

    /**
     *
     * @param photolatitude
     * The photolatitude
     */
    public void setPhotolatitude(double photolatitude) {
        this.photolatitude = photolatitude;
    }

    /**
     *
     * @return
     * The photolongitude
     */
    public double getPhotolongitude() {
        return photolongitude;
    }

    /**
     *
     * @param photolongitude
     * The photolongitude
     */
    public void setPhotolongitude(double photolongitude) {
        this.photolongitude = photolongitude;
    }

    /**
     *
     * @return
     * The rescheduleRef
     */
    public String getRescheduleRef() {
        return rescheduleRef;
    }

    /**
     *
     * @param rescheduleRef
     * The rescheduleRef
     */
    public void setRescheduleRef(String rescheduleRef) {
        this.rescheduleRef = rescheduleRef;
    }

    /**
     *
     * @return
     * The revokeRef
     */
    public String getRevokeRef() {
        return revokeRef;
    }

    /**
     *
     * @param revokeRef
     * The revokeRef
     */
    public void setRevokeRef(String revokeRef) {
        this.revokeRef = revokeRef;
    }

    /**
     *
     * @return
     * The reattemp
     */
    public long getReattemp() {
        return reattemp;
    }

    /**
     *
     * @param reattemp
     * The reattemp
     */
    public void setReattemp(long reattemp) {
        this.reattemp = reattemp;
    }

    /**
     *
     * @return
     * The showMerchant
     */
    public long getShowMerchant() {
        return showMerchant;
    }

    /**
     *
     * @param showMerchant
     * The showMerchant
     */
    public void setShowMerchant(long showMerchant) {
        this.showMerchant = showMerchant;
    }

    /**
     *
     * @return
     * The showShop
     */
    public long getShowShop() {
        return showShop;
    }

    /**
     *
     * @param showShop
     * The showShop
     */
    public void setShowShop(long showShop) {
        this.showShop = showShop;
    }

    /**
     *
     * @return
     * The isPickup
     */
    public long getIsPickup() {
        return isPickup;
    }

    /**
     *
     * @param isPickup
     * The isPickup
     */
    public void setIsPickup(long isPickup) {
        this.isPickup = isPickup;
    }

    /**
     *
     * @return
     * The isImport
     */
    public long getIsImport() {
        return isImport;
    }

    /**
     *
     * @param isImport
     * The isImport
     */
    public void setIsImport(long isImport) {
        this.isImport = isImport;
    }

    /**
     *
     * @return
     * The isApi
     */
    public long getIsApi() {
        return isApi;
    }

    /**
     *
     * @param isApi
     * The isApi
     */
    public void setIsApi(long isApi) {
        this.isApi = isApi;
    }

    /**
     *
     * @return
     * The orderSrc
     */
    public String getOrderSrc() {
        return orderSrc;
    }

    /**
     *
     * @param orderSrc
     * The orderSrc
     */
    public void setOrderSrc(String orderSrc) {
        this.orderSrc = orderSrc;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }
}