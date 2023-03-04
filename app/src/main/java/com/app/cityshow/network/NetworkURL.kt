package com.app.cityshow.network

object NetworkURL {
    // network constants
    const val DEVICE_TYPE_ANDROID = "Android"
    const val DEVICE_TYPE_IOS = "iOS"

    //headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val QUERY_AUTHORIZATION = "api_token"
    const val BEARER = "Bearer"
    const val CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"
    const val CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded"

    // response code
    const val RESPONSE_OK = 200
    const val RESPONSE_CREATED = 201
    const val RES_NOT_FOUND = 404
    const val RES_UNAUTHORISED = 401
    const val RES_BLOCKED = 402
    const val RES_FORBIDDEN = 403
    const val RES_UNPROCESSABLE_ENTITY = 422
    const val RES_SERVER_ERROR = 500
    const val RES_USER_INACTIVE = 400

    const val ACTION_FOR_BIDDEN_RESPONSE = "action_for_bidden_response"
    const val ACTION_FOR_BLOCKED = "action_for_blocked"
    const val ACTION_FOR_INACTIVE_USER = "action_for_inactive_user"


    const val LOGIN = "User/UserLogin"
    const val GET_PROFILE = "Master/GetUserProfile"
    const val GET_PART = "Master/GetPart"
    const val GET_LOCATIONS = "Master/GetLocation"
    const val GET_ASSET_TYPES = "Master/GetAssetType"
    const val GET_ASSET_CATEGORY = "Master/GetAssetCategory"
    const val ASSET_REGISTRATION = "Master/SetAssetRegistration"
    const val INVENTORY_DATA = "Report/InventoryData"
    const val CHECK_IN_CHECK_OUT = "Transaction/SetCheckinCheckout"

    const val SUBSCRIPTION_LIST = "plan/list"
    const val EPHEMERAL_KEY = "stripe/ephemeral_keys"
    const val PAYMENT_INTENT = "stripe/paymentIntent"
    const val CONFIRM_INTENT = "stripe/confirmIntent"
    const val CAPTURE_INTENT = "stripe/captureIntent"
}