package com.vandenbussche.emiel.projectsbp.api;


import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.PageRequest;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.models.requests.SearchRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PageResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Stijn on 7/09/2016.
 */
public interface IVotasticApiService {

    @GET("/api/my/polls")
    Observable<List<PollResponse>> getMyPolls();

    @POST("/api/my/polls")
    Observable<PollResponse> saveNewPoll(@Body PollRequest pollrequest);

    @POST("/api/my/pages")
    Observable<PageResponse> saveNewPage(@Body PageRequest pagerequest);

    @GET("/api/polls/random")
    Observable<List<PollResponse>> getRandomPolls();

    @GET("/api/polls/find")
    Observable<List<PollResponse>> getFindPolls(@Query("text") String searchText, @Query("maxUploadTime") long maxUploadTime);

    @GET("/api/polls/news")
    Observable<List<PollResponse>> getNewsPolls(@Query("maxUploadTime") long maxUploadTime);

    @POST("/api/my/follows")
    Observable<String> addFollow(@Query("pageId") String pageId);

    @DELETE("/api/my/follows")
    Observable<String> deleteFollow(@Query("pageId") String pageId);

    @GET("/api/polls/pageid")
    Observable<List<PollResponse>> getPollsByPageId(@Query("pageId") String pageId, @Query("maxUploadTime") long maxUploadTime);
}
