package com.vandenbussche.emiel.projectsbp.api;


import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.PageRequest;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PageResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
}
