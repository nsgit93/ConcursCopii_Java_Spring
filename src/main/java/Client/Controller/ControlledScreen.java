package Client.Controller;

import Services.IService;

public interface ControlledScreen {
    public void setScreenParent(ScreenSwitcher screenPage);
    public void setService(IService service);

}
