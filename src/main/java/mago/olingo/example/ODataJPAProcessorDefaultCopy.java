package mago.olingo.example;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderBatchProperties;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.PathInfo;
import org.apache.olingo.odata2.api.uri.info.*;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAProcessor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by aleoz on 12/1/16.
 */
public class ODataJPAProcessorDefaultCopy extends ODataJPAProcessor {
    public ODataJPAProcessorDefaultCopy(ODataJPAContext oDataJPAContext) {
        super(oDataJPAContext);
        if(oDataJPAContext == null) {
            throw new IllegalArgumentException("OData JPA Context cannot be null");
        }
    }

    public ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView, String contentType) throws ODataException {
        List jpaEntities = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, jpaEntities, contentType);
        this.close();
        return oDataResponse;
    }

    public ODataResponse readEntity(GetEntityUriInfo uriParserResultView, String contentType) throws ODataException {
        Object jpaEntity = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, jpaEntity, contentType);
        return oDataResponse;
    }

    public ODataResponse countEntitySet(GetEntitySetCountUriInfo uriParserResultView, String contentType) throws ODataException {
        long jpaEntityCount = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(jpaEntityCount);
        return oDataResponse;
    }

    public ODataResponse existsEntity(GetEntityCountUriInfo uriInfo, String contentType) throws ODataException {
        long jpaEntityCount = this.jpaProcessor.process(uriInfo);
        ODataResponse oDataResponse = this.responseBuilder.build(jpaEntityCount);
        return oDataResponse;
    }

    public ODataResponse createEntity(PostUriInfo uriParserResultView, InputStream content, String requestContentType, String contentType) throws ODataException {
        Object createdJpaEntity = this.jpaProcessor.process(uriParserResultView, content, requestContentType);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, createdJpaEntity, contentType);
        return oDataResponse;
    }

    public ODataResponse updateEntity(PutMergePatchUriInfo uriParserResultView, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException {
        Object jpaEntity = this.jpaProcessor.process(uriParserResultView, content, requestContentType);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, jpaEntity);
        return oDataResponse;
    }

    public ODataResponse deleteEntity(DeleteUriInfo uriParserResultView, String contentType) throws ODataException {
        Object deletedObj = this.jpaProcessor.process(uriParserResultView, contentType);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, deletedObj);
        return oDataResponse;
    }

    public ODataResponse executeFunctionImport(GetFunctionImportUriInfo uriParserResultView, String contentType) throws ODataException {
        List resultEntity = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, resultEntity, contentType);
        return oDataResponse;
    }

    public ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriParserResultView, String contentType) throws ODataException {
        List result = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, result, contentType);
        return oDataResponse;
    }

    public ODataResponse readEntityLink(GetEntityLinkUriInfo uriParserResultView, String contentType) throws ODataException {
        Object jpaEntity = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, jpaEntity, contentType);
        return oDataResponse;
    }

    public ODataResponse readEntityLinks(GetEntitySetLinksUriInfo uriParserResultView, String contentType) throws ODataException {
        List jpaEntity = this.jpaProcessor.process(uriParserResultView);
        ODataResponse oDataResponse = this.responseBuilder.build(uriParserResultView, jpaEntity, contentType);
        return oDataResponse;
    }

    public ODataResponse createEntityLink(PostUriInfo uriParserResultView, InputStream content, String requestContentType, String contentType) throws ODataException {
        this.jpaProcessor.process(uriParserResultView, content, requestContentType, contentType);
        return ODataResponse.newBuilder().build();
    }

    public ODataResponse updateEntityLink(PutMergePatchUriInfo uriParserResultView, InputStream content, String requestContentType, String contentType) throws ODataException {
        this.jpaProcessor.process(uriParserResultView, content, requestContentType, contentType);
        return ODataResponse.newBuilder().build();
    }

    public ODataResponse deleteEntityLink(DeleteUriInfo uriParserResultView, String contentType) throws ODataException {
        this.jpaProcessor.process(uriParserResultView, contentType);
        return ODataResponse.newBuilder().build();
    }

    public ODataResponse executeBatch(BatchHandler handler, String contentType, InputStream content) throws ODataException {
        ArrayList batchResponseParts = new ArrayList();
        PathInfo pathInfo = this.getContext().getPathInfo();
        EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();
        List batchParts = EntityProvider.parseBatchRequest(contentType, content, batchProperties);
        Iterator i$ = batchParts.iterator();

        while(i$.hasNext()) {
            BatchRequestPart batchPart = (BatchRequestPart)i$.next();
            batchResponseParts.add(handler.handleBatchPart(batchPart));
        }

        ODataResponse batchResponse = EntityProvider.writeBatchResponse(batchResponseParts);
        return batchResponse;
    }

    public BatchResponsePart executeChangeSet(BatchHandler handler, List<ODataRequest> requests) throws ODataException {
        ArrayList responses = new ArrayList();
        Iterator i$ = requests.iterator();

        while(i$.hasNext()) {
            ODataRequest request = (ODataRequest)i$.next();
            ODataResponse response = handler.handleRequest(request);
            if(response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode()) {
                ArrayList errorResponses = new ArrayList(1);
                errorResponses.add(response);
                return BatchResponsePart.responses(errorResponses).changeSet(false).build();
            }

            responses.add(response);
        }

        return BatchResponsePart.responses(responses).changeSet(true).build();
    }
}
