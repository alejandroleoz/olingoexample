package mago.olingo.example.olingo.jpa.extension;

import mago.olingo.example.db.MySQLConnection;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataErrorCallback;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.core.exception.ODataRuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAErrorCallback;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAAccessFactory;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAFactory;

import javax.persistence.EntityManagerFactory;

/**
 * Created by aleoz on 11/24/16.
 */
public class ExtendedODataJPAServiceFactory extends ODataServiceFactory {
    private ODataJPAContext oDataJPAContext;
    private ODataContext oDataContext;
    private boolean setDetailErrors = false;
    private OnJPAWriteContent onJPAWriteContent = null;

    public ExtendedODataJPAServiceFactory() {
    }

    public final ODataService createService(ODataContext ctx) throws ODataException {
        this.oDataContext = ctx;
        this.oDataJPAContext = this.initializeODataJPAContext();
        this.validatePreConditions();
        ODataJPAFactory factory = ODataJPAFactory.createFactory();
        ODataJPAAccessFactory accessFactory = factory.getODataJPAAccessFactory();
        if(this.oDataJPAContext.getODataContext() == null) {
            this.oDataJPAContext.setODataContext(ctx);
        }

        /******************
         * CUSTOM CHANGE HERE
         ************************/
//        ODataSingleProcessor odataJPAProcessor = accessFactory.createODataProcessor(this.oDataJPAContext);
        ODataSingleProcessor odataJPAProcessor = new CustomODataJPAProcessor(oDataJPAContext);


        EdmProvider edmProvider = accessFactory.createJPAEdmProvider(this.oDataJPAContext);
        return this.createODataSingleProcessorService(edmProvider, odataJPAProcessor);
    }

    private void validatePreConditions() throws ODataJPARuntimeException {
        if(this.oDataJPAContext.getEntityManagerFactory() == null) {
            throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.ENTITY_MANAGER_NOT_INITIALIZED, (Throwable)null);
        }
    }


    /********************************************
        THIS IS CUSTOM
     ***********************************************/
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
        ODataJPAContext oDataJPAContext = this.getODataJPAContext();
        EntityManagerFactory emf;
        try {
            emf = MySQLConnection.getInstance().getEntityManager().getEntityManagerFactory();
            oDataJPAContext.setEntityManagerFactory(emf);
            oDataJPAContext.setPersistenceUnitName(MySQLConnection.PERSISTENCE_UNIT);
            oDataJPAContext.setJPAEdmExtension(new MyJPAEdmExtension());
            oDataJPAContext.setJPAEdmMappingModel("META-INF/JPAEdmMapping.xml");
            return oDataJPAContext;
        } catch (Exception e) {
            throw new ODataRuntimeException(e);
        }
    };

    public final ODataJPAContext getODataJPAContext() throws ODataJPARuntimeException {
        if(this.oDataJPAContext == null) {
            this.oDataJPAContext = ODataJPAFactory.createFactory().getODataJPAAccessFactory().createODataJPAContext();
        }

        if(this.oDataContext != null) {
            this.oDataJPAContext.setODataContext(this.oDataContext);
        }

        return this.oDataJPAContext;
    }

    protected void setDetailErrors(boolean setDetailErrors) {
        this.setDetailErrors = setDetailErrors;
    }

    protected void setOnWriteJPAContent(OnJPAWriteContent onJPAWriteContent) {
        this.onJPAWriteContent = onJPAWriteContent;
    }

    public <T extends ODataCallback> T getCallback(Class<? extends ODataCallback> callbackInterface) {
        return (T) (this.setDetailErrors && callbackInterface.isAssignableFrom(ODataErrorCallback.class)?new ODataJPAErrorCallback():(this.onJPAWriteContent != null && callbackInterface.isAssignableFrom(OnJPAWriteContent.class)?this.onJPAWriteContent:null));
    }
}
