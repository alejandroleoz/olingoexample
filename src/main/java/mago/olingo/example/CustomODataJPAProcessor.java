package mago.olingo.example;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAProcessor;

import java.util.List;

public class CustomODataJPAProcessor extends ODataJPAProcessor {

    public CustomODataJPAProcessor(ODataJPAContext oDataJPAContext) {
        super(oDataJPAContext);
    }

    @Override
    public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView, final String contentType) throws ODataException {

       /* Pre Process Step */
        preprocess();

        List<Object> jpaEntities = jpaProcessor.process(uriParserResultView);

       /* Post Process Step */
        postProcess();

        ODataResponse oDataResponse = responseBuilder.build(uriParserResultView, jpaEntities, contentType);

        return oDataResponse;
    }

    @Override
    public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {
        Object entity = jpaProcessor.process(uriInfo);

        ODataResponse oDataResponse = responseBuilder.build(uriInfo, entity, contentType);

        return oDataResponse;
    }

    private void preprocess() {
        System.out.println("");
    }

    private void postProcess(){
        System.out.println("");
    }
}
