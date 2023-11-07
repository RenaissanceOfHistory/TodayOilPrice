package today.oil.constant;

/** 触发 Sentinel 时调用常量
 * @create: 2023/10/30
 * @Description:
 * @FileName: BlockHandlerConstants
 */
public interface BlockHandlerConstants {
    // UserController
    String BH_LOGIN = "bh_login";
    String BH_ENROLL = "bh_enroll";
    String BH_QUERY_USER_DETAIL = "bh_queryUserDetail";
    String BH_UPDATE_USER_DETAIL = "bh_updateUserDetail";
    String BH_REMOVE_USER = "bh_removeUser";
    String BH_CODE = "bh_getCode";

    // CarController
    String BH_QUERY_CAR_BY_UID = "bh_queryCarByUid";
    String BH_ADD_CAR = "bh_addCar";
    String BH_UPDATE_CAR = "bh_updateCar";
    String BH_REMOVE_CAR = "bh_removeCar";
    String BH_REMOVE_CAR_BY_UID = "bh_removeCarByUid";
    
    // FareController
    String BH_QUERY_FARE_BY_CID = "bh_queryFareByCid";
    String BH_ADD_FARE = "bh_addFare";
    String BH_UPDATE_FARE = "bh_updateFare";
    String BH_REMOVE_FARE = "bh_removeFare";
    String BH_REMOVE_FARE_BY_CID = "bh_removeFareByCid";

    // AddOilController
    String BH_QUERY_ADD_OIL_BY_CID = "bh_queryAddOilByCid";
    String BH_ADD_ADD_OIL = "bh_addAddOil";
    String BH_UPDATE_ADD_OIL = "bh_updateAddOil";
    String BH_REMOVE_ADD_OIL_BY_ID = "bh_removeAddOilById";
    String BH_REMOVE_ADD_OIL_BY_CID = "bh_removeAddOilByCid";
    String BH_QUERY_MAX_MILE_BY_CID = "bh_queryMaxMileByCid";

    // OilPriceController
    String BH_QUERY_PROVINCES = "bh_queryProvinces";
    String BH_QUERY_OIL_PRICE_BY_PROVINCE = "bh_queryOilPriceByProvince";
    String BH_QUERY_OIL_PRICE_BY_LABEL = "bh_queryOilPriceByLabel";
}
