package mago.olingo.example.model.odata;

import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;

import java.util.Map;

/**
 * Created by aleoz on 11/30/16.
 */
public interface EntityAdapter<T> {

    public EntityType getEntityType();

    public EntitySet getEntitySet(String namespace);

    public T createEntityFromProperties(Map<String, Object> properties);
}
