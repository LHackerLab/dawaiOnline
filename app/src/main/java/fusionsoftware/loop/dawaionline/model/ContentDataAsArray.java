package fusionsoftware.loop.dawaionline.model;

/**
 * Created by user on 8/9/2017.
 */

public class ContentDataAsArray {

    private Response response;

    private Data[] data;

    public Result[] getResults() {
        return result;
    }

    public void setResults(Result[] results) {
        this.result = results;
    }

    private Result[] result;

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
