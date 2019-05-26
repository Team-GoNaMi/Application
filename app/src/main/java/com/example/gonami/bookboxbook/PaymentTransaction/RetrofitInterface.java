package com.example.gonami.bookboxbook.PaymentTransaction;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * http call을 위한 retrofit2 interface 정의
 *
 * Created by LeeHyeonJae on 2017-06-27.
 */
public interface RetrofitInterface {


    /**
     * token 획득
     *
     * @param params
     * @return
     */
    @FormUrlEncoded // POST에만 붙는다고 한다.
    @POST("/oauth/2.0/token")
    Call<Map> token(@FieldMap Map<String, String> params);

    /**
     * 사용자정보조회
     *
     * @param params
     * @return
     */
    @GET("/v1.0/user/me")
    Call<Map> userMe(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    /**
     * 잔액조회
     *
     * @param params
     * @return
     */
    @GET("/v1.0/account/balance")
    Call<Map> accountBalance(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    /**
     * 거래내역조회
     *
     * @param params
     * @return
     */
    @GET("/v1.0/account/transaction_list")
    Call<Map> accountTrasactionList(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    /**
     * 계좌실명조회
     *
     * @param params
     * @return
     */
    @POST("/v1.0/inquiry/real_name")
    Call<Map> inquiryRealName(@Header("Authorization") String token, @Body Map<String, String> params);

    /**
     * 출금이체
     *
     * @param params
     * @return
     */
    @POST("/v1.0/transfer/withdraw")
    Call<Map> trasnferWithdraw(@Header("Authorization") String token, @Body Map<String, String> params);

    /**
     * 입금이체(핀테크이용번호)
     *
     * @param params
     * @return
     */
    @POST("/v1.0/transfer/deposit")
    Call<Map> transferDeposit(@Header("Authorization") String token, @Body Map<String, String> params);

    /**
     * 입금이체(계좌번호)
     *
     * @param params
     * @return
     */
    @POST("/v1.0/transfer/deposit2")
    Call<Map> transferDeposit2(@Header("Authorization") String token, @Body Map<String, String> params);
}
