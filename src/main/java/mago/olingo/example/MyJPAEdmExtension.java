package mago.olingo.example;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.*;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyJPAEdmExtension implements JPAEdmExtension {

    public void extendWithOperation(JPAEdmSchemaView view) {
    }

    public void extendJPAEdmSchema(JPAEdmSchemaView view) {
        Schema edmSchema = view.getEdmSchema();

        // add custom entity
        edmSchema.getEntityTypes().add(this.createPriceEntityType());

        // add custom entity set
        for(EntityContainer container : edmSchema.getEntityContainers()){
            if(container.getName().equals("mysql_PUContainer")){
                container.getEntitySets().add(this.createProceEntitySet());
            }
        }

    }

    public InputStream getJPAEdmMappingModelStream() {
        return null;
    }



    private EntityType createPriceEntityType() {

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

    private EntitySet createProceEntitySet() {
        EntitySet set = new EntitySet();
        set.setName("OrdenSet");
        set.setEntityType(new FullQualifiedName("mysql_PU", "Orden"));
        return set;
    }
}
