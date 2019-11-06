package am.newway.aca.api;

        import am.newway.aca.api.model.Item;
        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Path;

public interface Service {
    @GET("users/{name}")
    Call<Item> getItems(@Path("name") String name);
}
