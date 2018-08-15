package fusionsoftware.loop.dawaionline.model;

/**
 * Created by user on 8/9/2017.
 */

public class ContentDataAsArray {

    private Response response;

    private Data[] data;

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }

    private Result[] results;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", data = " + data + "]";
    }
}
