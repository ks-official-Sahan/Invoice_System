package ewision.sahan.menu;

/**
 *
 * @author ks.official.sahan
 */
public class MenuAction {

    protected boolean isCancel() {
        return cancel;
    }

    public void cancel() {
        this.cancel = true;
    }

    private boolean cancel = false;
}
