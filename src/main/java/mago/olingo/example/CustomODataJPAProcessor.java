package mago.olingo.example;

import mago.olingo.example.model.Price;
import mago.olingo.example.model.odata.PriceEntityAdapter;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomODataJPAProcessor extends ODataJPAProcessorDefaultCopy {

    private List<String> hybridEntities = new ArrayList<String>();
    private PriceEntityAdapter priceAdapter = new PriceEntityAdapter();


    public CustomODataJPAProcessor(ODataJPAContext oDataJPAContext) {
        super(oDataJPAContext);

        // TODO: get hybrid entities from another module
        hybridEntities.add("Price");
    }

    @Override
    public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView, final String contentType) throws ODataException {

        if(this.isHybridEntity(uriParserResultView.getStartEntitySet().getEntityType().getName())) {

            List<Object> nonJPAEntities = new ArrayList<Object>();

            // TODO: parse input and get entities from datasource
            nonJPAEntities.add(new Price(99L, 123, "USD"));

            ODataResponse oDataResponse = responseBuilder.build(uriParserResultView, nonJPAEntities, contentType);
            return oDataResponse;
        } else {
            return super.readEntitySet(uriParserResultView, contentType);
        }
    }

    @Override
    public ODataResponse createEntity(PostUriInfo uriParserResultView, InputStream content, String requestContentType, String contentType) throws ODataException {
        if(this.isHybridEntity(uriParserResultView.getStartEntitySet().getEntityType().getName())){
            EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();
            ODataEntry oDataEntry = EntityProvider.readEntry(requestContentType, uriParserResultView.getStartEntitySet(), content, properties);

            // todo: parse input and create corresponding entity
            Price price = priceAdapter.createEntityFromProperties(oDataEntry.getProperties());

            return this.responseBuilder.build(uriParserResultView, price, contentType);
        }else{
            return super.createEntity(uriParserResultView, content, requestContentType, contentType);
        }
    }

    @Override
    public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {
        if(this.isHybridEntity(uriInfo.getStartEntitySet().getName())){

            // TODO: parse input and get entity from datasource
            Price price = new Price(99L, 123, "USD");

            return responseBuilder.build(uriInfo, price, contentType);
        } else {
            return super.readEntity(uriInfo, contentType);
        }
    }


    private boolean isHybridEntity(String entityName) {
        return hybridEntities.contains(entityName);
    }





}
