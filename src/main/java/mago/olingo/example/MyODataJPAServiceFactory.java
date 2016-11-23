package mago.olingo.example;

import mago.olingo.example.db.MySQLConnection;
import org.apache.olingo.odata2.core.exception.ODataRuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

import javax.persistence.EntityManagerFactory;

/**
 * Created by aleoz on 11/22/16.
 */
public class MyODataJPAServiceFactory extends ODataJPAServiceFactory {

    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {

        ODataJPAContext oDataJPAContext = this.getODataJPAContext();
        EntityManagerFactory emf;
        try {
            emf = MySQLConnection.getInstance().getEntityManager().getEntityManagerFactory();
            oDataJPAContext.setEntityManagerFactory(emf);
            oDataJPAContext.setPersistenceUnitName(MySQLConnection.PERSISTENCE_UNIT);
//            oDataJPAContext.setJPAEdmExtension(new EspmProcessingExtension());
            oDataJPAContext.setJPAEdmMappingModel("META-INF/JPAEdmMapping.xml");
            return oDataJPAContext;
        } catch (Exception e) {
            throw new ODataRuntimeException(e);
        }

    }

}
