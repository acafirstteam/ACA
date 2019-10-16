package am.newway.aca.ui.fragments.api;

        import am.newway.aca.ui.fragments.model.ItemResponse;
        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Path;

public interface Service {
    @GET("users/{name}")
    Call<ItemResponse> getItems(@Path("name") String name);
}
