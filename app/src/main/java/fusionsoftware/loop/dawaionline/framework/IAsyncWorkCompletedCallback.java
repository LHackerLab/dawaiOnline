package fusionsoftware.loop.dawaionline.framework;

/**
 * Created by lalit on 7/25/2017.
 */
public interface IAsyncWorkCompletedCallback {
    public void onDone(String workName, boolean isComplete);
}
