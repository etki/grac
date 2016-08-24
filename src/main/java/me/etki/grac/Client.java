package me.etki.grac;

import me.etki.grac.common.Action;
import me.etki.grac.utility.TypeSpec;

import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Client {

    RequestOptions getDefaultRequestOptions();
    <I, O> CompletableFuture<Response<O>> execute(Request<I> request, TypeSpec expectedType, RequestOptions options);
    
    
    default <I, O> CompletableFuture<Response<O>> execute(Request<I> request, TypeSpec expectedType) {
        return execute(request, expectedType, getDefaultRequestOptions());
    }
    
    
    default <O> CompletableFuture<Response<O>> read(String resource, TypeSpec expectedType, RequestOptions options) {
        return execute(new Request<>(resource, Action.READ), expectedType, options);
    }
    default <O> CompletableFuture<Response<O>> read(String resource, TypeSpec expectedType) {
        return execute(new Request<>(resource, Action.READ), expectedType, getDefaultRequestOptions());
    }
    
    
    default <O> CompletableFuture<Response<O>> create(String resource, TypeSpec expectedType, RequestOptions options) {
        return execute(new Request<>(resource, Action.CREATE), expectedType, options);
    }
    default <O> CompletableFuture<Response<O>> create(String resource, TypeSpec expectedType) {
        return create(resource, expectedType, getDefaultRequestOptions());
    }
    default <I, O> CompletableFuture<Response<O>> create(String resource, I payload, TypeSpec expectedType, 
                                                         RequestOptions options) {
        
        return execute(new Request<>(resource, Action.CREATE, payload), expectedType, options);
    }
    default <I, O> CompletableFuture<Response<O>> create(String resource, I payload, TypeSpec expectedType) {
        return create(resource, payload, expectedType, getDefaultRequestOptions());
    }
    
    
    default <O> CompletableFuture<Response<O>> set(String resource, TypeSpec expectedType, RequestOptions options) {
        return execute(new Request<>(resource, Action.SET), expectedType, options);
    }
    default <O> CompletableFuture<Response<O>> set(String resource, TypeSpec expectedType) {
        return set(resource, expectedType, getDefaultRequestOptions());
    }
    default <I, O> CompletableFuture<Response<O>> set(String resource, I payload, TypeSpec expectedType, 
                                                         RequestOptions options) {
        
        return execute(new Request<>(resource, Action.SET, payload), expectedType, options);
    }
    default <I, O> CompletableFuture<Response<O>> set(String resource, I payload, TypeSpec expectedType) {
        return set(resource, payload, expectedType, getDefaultRequestOptions());
    }
    
    
    default <O> CompletableFuture<Response<O>> modify(String resource, TypeSpec expectedType, RequestOptions options) {
        return execute(new Request<>(resource, Action.MODIFY), expectedType, options);
    }
    default <O> CompletableFuture<Response<O>> modify(String resource, TypeSpec expectedType) {
        return modify(resource, expectedType, getDefaultRequestOptions());
    }
    default <I, O> CompletableFuture<Response<O>> modify(String resource, I payload, TypeSpec expectedType, 
                                                         RequestOptions options) {
        
        return execute(new Request<>(resource, Action.MODIFY, payload), expectedType, options);
    }
    default <I, O> CompletableFuture<Response<O>> modify(String resource, I payload, TypeSpec expectedType) {
        return modify(resource, payload, expectedType, getDefaultRequestOptions());
    }


    default <O> CompletableFuture<Response<O>> delete(String resource, TypeSpec expectedType, RequestOptions options) {
        return execute(new Request<>(resource, Action.DELETE), expectedType, options);
    }
    default <O> CompletableFuture<Response<O>> delete(String resource, TypeSpec expectedType) {
        return execute(new Request<>(resource, Action.DELETE), expectedType, getDefaultRequestOptions());
    }
}
