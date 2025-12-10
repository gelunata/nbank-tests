package requests.skelethon.interfaces;

import models.BaseModel;

public interface CrudEndpointInterface {
    Object post(BaseModel model);
    Object get(BaseModel model);
    Object update(long id);
    Object delete(long id);
}
