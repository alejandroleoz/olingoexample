package mago.olingo.example.model.odata;

import mago.olingo.example.model.Price;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PriceEntityAdapter implements EntityAdapter<Price>{

    public EntityType getEntityType() {
        List<Property> properties = new ArrayList<Property>();
        SimpleProperty property;

        property = new SimpleProperty();
        property.setName("Id");
        property.setType(EdmSimpleTypeKind.Int32);
        properties.add(property);

        property = new SimpleProperty();
        property.setName("Amount");
        property.setType(EdmSimpleTypeKind.Double);
        properties.add(property);

        property = new SimpleProperty();
        property.setName("Currency");
        property.setType(EdmSimpleTypeKind.String);
        properties.add(property);

        PropertyRef propertyRef = new PropertyRef();
        propertyRef.setName("Id");
        Key key = new Key();
        key.setKeys(Arrays.asList(propertyRef));

        EntityType type = new EntityType();
        type.setKey(key);
        type.setName("Price");
        type.setProperties(properties);
        return type;
    }

    public EntitySet getEntitySet(String namespace) {
        EntitySet set = new EntitySet();
        set.setName("OrdenSet");
        set.setEntityType(new FullQualifiedName("mysql_PU", "Orden"));
        return set;
    }

    public Price createEntityFromProperties(Map<String, Object> properties) {
        return new Price(888L, (Double)properties.get("Amount"), (String)properties.get("Currency"));
    }
}
